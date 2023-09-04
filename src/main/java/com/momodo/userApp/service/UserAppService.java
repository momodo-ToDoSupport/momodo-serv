package com.momodo.userApp.service;

import com.momodo.aws.S3UploadService;
import com.momodo.jwt.dto.BasicResponse;
import com.momodo.jwt.exception.error.DuplicateMemberException;
import com.momodo.jwt.security.util.SecurityUtil;
import com.momodo.userApp.domain.Tier;
import com.momodo.userApp.domain.UserApp;
import com.momodo.userApp.domain.UserType;
import com.momodo.userApp.dto.RequestCreateUserApp;
import com.momodo.userApp.dto.RequestUpdateUserProfile;
import com.momodo.userApp.dto.ResponseUserApp;
import com.momodo.userApp.repository.UserAppRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserAppService {

    private final UserAppRepository userAppRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final S3UploadService s3UploadService;

    public UserAppService(UserAppRepository userAppRepository, PasswordEncoder passwordEncoder, SecurityUtil securityUtil, S3UploadService s3UploadService) {
        this.userAppRepository = userAppRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
        this.s3UploadService = s3UploadService;
    }

    @Transactional
    public void signup(RequestCreateUserApp createUserApp) {
        if (userAppRepository.findOneWithAuthoritiesByUserId(createUserApp.getUserId()).orElseGet(() -> null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        UserApp userApp = UserApp.builder()
                .userId(createUserApp.getUserId())
                .password(createUserApp.getPassword())
                .type(UserType.MOMODO)
                .name(createUserApp.getName())
                .phone(createUserApp.getPhone())
                .email(createUserApp.getEmail())
                .isActive(true)
                .isPush(true)
                .password(createUserApp.getPassword())
                .build();

        userAppRepository.save(userApp);
    }

    //userId를 파라미터로 받아 해당 유저의 정보를 가져온다.
    @Transactional(readOnly = true)
    public ResponseUserApp.Info getUserAppWithAuthorities(String userId) {
        UserApp getUserApp = userAppRepository.findOneWithAuthoritiesByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        String imageUrl = s3UploadService.getS3Url(getUserApp.getProfileImage());
        return ResponseUserApp.Info.of(getUserApp, imageUrl);
    }

    @Transactional(readOnly = true)
    public ResponseUserApp.Info getMyUserAppAuthorities() {
        UserApp getUserApp = securityUtil.getCurrentUsername()
                .flatMap(userAppRepository::findOneWithAuthoritiesByUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        String imageUrl = s3UploadService.getS3Url(getUserApp.getProfileImage());
        return ResponseUserApp.Info.of(getUserApp, imageUrl);
    }

    @Transactional
    public void updateProfile(String userId, MultipartFile file, RequestUpdateUserProfile updateDto) {
        UserApp getUserApp = userAppRepository.findOneWithAuthoritiesByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        if(file.isEmpty()){
            getUserApp.updateProfile(getUserApp.getProfileImage(), updateDto);
        }else{
            try{
                // 기존 프로필 이미지는 버킷에서 제거
                s3UploadService.deleteFile(getUserApp.getProfileImage());

                // 매개변수로 넘어온 이미지를 버킷에 추가
                String updateFileName = s3UploadService.uploadFile(file);
                getUserApp.updateProfile(updateFileName, updateDto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void updateTier(String userId, Tier tier) {
        UserApp getUserApp = userAppRepository.findOneWithAuthoritiesByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        getUserApp.setTodoTier(tier);
    }

    @Transactional
    public void resetAllUserTiers(){
        userAppRepository.resetAllUserTiers();
    }
}