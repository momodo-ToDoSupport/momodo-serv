package com.momodo.userApp.service;

import com.momodo.userApp.domain.UserApp;
import com.momodo.userApp.domain.UserAppAdapter;
import com.momodo.userApp.repository.UserAppRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAppRepository userAppRepository;

    public CustomUserDetailsService(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    /**
     * "인증 API 호출 시에만" 그 과정에서 재정의한 loadUserByUsername를 호출하여 디비에서 유저정보와 권한정보를 가져온다.
     * 내가 만든 커스텀 UserAdapter는 org.springframework.security.core.userdetails.User를 상속받았으므로 이걸 반환해도 된다.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String userId) {
        UserApp userApp = userAppRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다."));

        if(!userApp.getIsActive()) throw new RuntimeException(userApp.getUserId() + " -> 활성화되어 있지 않습니다.");
        return new UserAppAdapter(userApp);
    }
}
