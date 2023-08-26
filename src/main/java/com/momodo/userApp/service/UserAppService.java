package com.momodo.userApp.service;

import com.momodo.jwt.dto.CommonResponse;
import com.momodo.jwt.exception.error.DuplicateMemberException;
import com.momodo.jwt.security.util.SecurityUtil;
import com.momodo.userApp.domain.Tier;
import com.momodo.userApp.domain.UserApp;
import com.momodo.userApp.domain.UserType;
import com.momodo.userApp.dto.RequestCreateUserApp;
import com.momodo.userApp.dto.ResponseUserApp;
import com.momodo.userApp.repository.UserAppRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAppService {

    private final UserAppRepository userAppRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    public UserAppService(UserAppRepository userAppRepository, PasswordEncoder passwordEncoder, SecurityUtil securityUtil) {
        this.userAppRepository = userAppRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
    }

    @Transactional
    public CommonResponse signup(RequestCreateUserApp createUserApp) {
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

        return CommonResponse.builder()
                .success(true)
                .response(null)
                .build();
    }

    //userId를 파라미터로 받아 해당 유저의 정보를 가져온다.
    @Transactional(readOnly = true)
    public ResponseUserApp.Info getUserAppWithAuthorities(String userId) {
        return ResponseUserApp.Info.of(userAppRepository.findOneWithAuthoritiesByUserId(userId).orElseGet(() -> null));
    }

    @Transactional(readOnly = true)
    public ResponseUserApp.Info getMyUserAppAuthorities() {
        return ResponseUserApp.Info.of(
                securityUtil.getCurrentUsername()
                        .flatMap(userAppRepository::findOneWithAuthoritiesByUserId)
                        .orElseGet(() -> null));
    }

    @Transactional
    public CommonResponse updateTier(String userId, Tier tier) {
        UserApp getUserApp = userAppRepository.findOneWithAuthoritiesByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        getUserApp.setTodoTier(tier);

        return CommonResponse.builder()
                .success(true)
                .response(null)
                .build();
    }

    @Transactional
    public void resetAllUserTiers(){
        userAppRepository.resetAllUserTiers();
    }
}