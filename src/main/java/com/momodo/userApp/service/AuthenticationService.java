package com.momodo.userApp.service;


import com.momodo.jwt.exception.error.InvalidRefreshTokenException;
import com.momodo.jwt.security.RefreshTokenProvider;
import com.momodo.jwt.security.TokenProvider;
import com.momodo.userApp.domain.TodoTier;
import com.momodo.userApp.domain.UserApp;
import com.momodo.userApp.domain.UserType;
import com.momodo.userApp.dto.ResponseAuthentication;
import com.momodo.userApp.repository.UserAppRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class AuthenticationService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserAppRepository userAppRepository;

    public AuthenticationService(TokenProvider tokenProvider, RefreshTokenProvider refreshTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserAppRepository userAppRepository) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userAppRepository = userAppRepository;
    }

    public ResponseAuthentication.Token authenticate(HashMap<String, Object> requestLogin) {
        // 받아온 adminId과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestLogin.get("id").toString(), requestLogin.get("password").toString());

        System.out.println("authenticationToken :: "+ authenticationToken);

        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailService에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createToken(authentication);

        // 위에서 loadUserByUsername를 호출하였으므로 UserAdminAdapter가 시큐리티 컨텍스트에 저자오디어 UserAdmin 엔티티 정보를 알 수 있다.
        // 유저 정보에서 중치를 꺼내 리프레시 토큰 가중치에 할당, 나중에 액세스토큰 재발급 시도 시 유저정보 가중치 > 리프레시 토큰이라면 실패
        System.out.println("authentication.getPrincipal() : " + authentication.getPrincipal());

        Long tokenWeight = 1L;
        String refreshToken = refreshTokenProvider.createToken(authentication, tokenWeight);

        UserApp getUserApp = userAppRepository.findByUserId(requestLogin.get("id").toString()).orElse(null);
        if (getUserApp == null) {
            UserApp userApp = UserApp.builder()
                    .userId(requestLogin.get("id").toString())
                    .type((UserType) requestLogin.get("loginType"))
                    .password(requestLogin.get("password").toString())
                    .name(requestLogin.get("nickname").toString())
                    .email(requestLogin.get("email").toString())
                    .phone(requestLogin.get("phone").toString())
                    .isActive(true)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            userAppRepository.save(userApp);
        }

        return ResponseAuthentication.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // adminId과 패스워드로 사용자를 인증하여 액세스토큰과 리프레시 토큰을 반환한다.
    public ResponseAuthentication.Token authenticateMomodo(String adminId, String password) {
        // 받아온 adminId과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(adminId, password);

        System.out.println("authenticationToken :: "+ authenticationToken);

        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailService에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createToken(authentication);

        // 위에서 loadUserByUsername를 호출하였으므로 UserAdminAdapter가 시큐리티 컨텍스트에 저자오디어 UserAdmin 엔티티 정보를 알 수 있다.
        // 유저 정보에서 중치를 꺼내 리프레시 토큰 가중치에 할당, 나중에 액세스토큰 재발급 시도 시 유저정보 가중치 > 리프레시 토큰이라면 실패
        System.out.println("authentication.getPrincipal() : " + authentication.getPrincipal());
        UserApp userApp = userAppRepository.findByUserId(String.valueOf(authentication.getPrincipal())).orElse(null);

        Long tokenWeight = userApp.getTokenWeight();
        String refreshToken = refreshTokenProvider.createToken(authentication, tokenWeight);

        return ResponseAuthentication.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseAuthentication.Token refreshToken(String refreshToken) {
        // 먼저 리프레시 토큰을 검증한다.
        if(!refreshTokenProvider.validateToken(refreshToken)) throw new InvalidRefreshTokenException();

        // 리프레시 토큰 값을 이용해 사용자를 꺼낸다.
        // refreshTokenProvider과 TokenProvider는 다은 서명키를 가지고 있기에 refreshTokenProvider를 써야한다.
        Authentication authentication = refreshTokenProvider.getAuthentication(refreshToken);
        UserApp userApp = userAppRepository.findOneWithAuthoritiesByUserId(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName() + "을 찾을 수 없습니다."));

        // 사용자 디비 값에 있는 것과 가중치 비교, 디비 가중치가 더 크다면 유효하지 않는다.
        if(userApp.getTokenWeight() > refreshTokenProvider.getTokenWeight(refreshToken))
            throw new InvalidRefreshTokenException();

        // 리프레시 토큰에 담긴 값을 그대로 액세스 토큰 생성에 활용한다.
        String accessToken = tokenProvider.createToken(authentication);
        // 기존 리프레시 토큰과 새로 만든 액세스 토큰을 반환한다.
        return ResponseAuthentication.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    // UserAdmin 가중치를 1 올림으로써 해당 adminId 리프레시토큰 무효화
    @Transactional
    public void invalidateRefreshTokenByUsername(String adminId) {
        UserApp userApp = userAppRepository.findOneWithAuthoritiesByUserId(adminId)
                .orElseThrow(() -> new UsernameNotFoundException(adminId + "-> 찾을 수 없습니다."));

        userApp.increaseTokenWeight();  // 더티체킹에 의해 엔티티 변화 반영
    }


}
