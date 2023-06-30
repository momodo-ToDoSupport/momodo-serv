package com.momodo.userApp.service;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.momodo.jwt.dto.CommonResponse;
import com.momodo.jwt.security.JwtFilter;
import com.momodo.userApp.domain.UserType;
import com.momodo.userApp.dto.RequestLogin;
import com.momodo.userApp.dto.ResponseAuthentication;
import com.momodo.userApp.dto.google.ConfigUtils;
import com.momodo.userApp.dto.google.GoogleLoginDto;
import com.momodo.userApp.dto.google.GoogleLoginRequest;
import com.momodo.userApp.dto.google.GoogleLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {

    @Value("${kakao.get.user.me.url}")
    private String kakaoReqURL;

    private final AuthenticationService authenticationService;
    private final ConfigUtils configUtils;

    public CommonResponse socialLogin(RequestLogin requestLogin) throws Exception {

        UserType loginType = requestLogin.getLoginType();
        ResponseAuthentication.Token token = new ResponseAuthentication.Token();
        if (loginType.equals(UserType.KAKAO)) {
            String accessToken = requestLogin.getAccessToken();
            HashMap<String, Object> userInfo = getKakaoUserInfo(accessToken);

            token = authenticationService.authenticate(userInfo);

            log.info("userInfo : " + userInfo);
        }

        if(loginType.equals(UserType.GOOGLE)) {
            String accessToken = requestLogin.getAccessToken();
            GoogleLoginDto googleUserInfo = getGoogleUserInfo(accessToken);
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", googleUserInfo.getSub());
            userInfo.put("loginType", UserType.GOOGLE);
            userInfo.put("nickname", googleUserInfo.getName());
            userInfo.put("email", googleUserInfo.getEmail());
            userInfo.put("password", "google" + googleUserInfo.getSub());
            userInfo.put("phone", "null");

            token = authenticationService.authenticate(userInfo);

            log.info("userInfo : " + userInfo);
        }


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());

        return CommonResponse.builder()
                .success(true)
                .response(token)
                .build();
    }

    public HashMap<String, Object> getKakaoUserInfo(String accessToken) {

        HashMap<String, Object> userInfo = new HashMap<>();

        try {
            URL url = new URL(kakaoReqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            log.info("responseCode : {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while((line = br.readLine()) != null) {
                result += line;
            }

            log.info("response body : " + result);
            log.info("result type" + result.getClass().getName());

            try {
                //jackson objectmapper 객체 생성
                ObjectMapper objectMapper = new ObjectMapper();
                //JSON String -> Map
                Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});

                log.info("properties : {}", jsonMap.get("properties"));

                Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
                Map<String, Object> kakaoAccount = (Map<String, Object>) jsonMap.get("kakao_account");

                log.info("properties : {} " , properties);
                log.info("kakaoAccount : {}", kakaoAccount);

                Long id = (Long) jsonMap.get("id");
                String nickname = (String) properties.get("nickname");
                String email = (String) kakaoAccount.get("email");
                String birthday = (String) kakaoAccount.get("birthday");
                String gender = (String) kakaoAccount.get("gender");

                userInfo.put("id", id);
                userInfo.put("nickname", nickname);
                userInfo.put("email", email);
                userInfo.put("loginType", UserType.KAKAO);
                userInfo.put("password", "kakao" + id);
                userInfo.put("phone", "01012345678");

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
           e.printStackTrace();
        }

        return userInfo;
    }

    public GoogleLoginDto getGoogleUserInfo(String authCode) {

        GoogleLoginDto userInfoDto = new GoogleLoginDto();

        // HTTP 통신을 위해 RestTemplate 활용
        RestTemplate restTemplate = new RestTemplate();
        GoogleLoginRequest requestParams = GoogleLoginRequest.builder()
                .clientId(configUtils.getGoogleClientId())
                .clientSecret(configUtils.getGoogleSecret())
                .code(authCode)
                .redirectUri(configUtils.getGoogleRedirectUri())
                .grantType("authorization_code")
                .build();

        try {
            // Http Header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleLoginRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getGoogleAuthUrl() + "/token", httpRequestEntity, String.class);

            // ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            GoogleLoginResponse googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponse>() {});

            // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(configUtils.getGoogleAuthUrl() + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            if(resultJson != null) {
                userInfoDto = objectMapper.readValue(resultJson, new TypeReference<GoogleLoginDto>() {});
            }
            else {
                throw new Exception("Google OAuth failed!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return userInfoDto;
    }


}
