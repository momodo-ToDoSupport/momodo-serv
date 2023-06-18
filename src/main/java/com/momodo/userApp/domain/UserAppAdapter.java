package com.momodo.userApp.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


import java.util.Collections;
import java.util.List;

/**
 * 인증 API는 그 과정에서 loadUserByUsername를 호출해 디비에서 사용자 정보를 꺼내온다.
 * 인증 API 호출 시 엔티티 유저를 반환하여 사용하고 싶어 어댑터 패턴 사용
 * authentication.getPrincipal()로 AccountAdapter 가져올 수 있다.
 */
public class UserAppAdapter extends User {
    private UserApp userApp;

    public UserAppAdapter(UserApp userApp) {
        super(userApp.getUserId(), userApp.getPassword(), authorities(userApp.getRoles()));
        this.userApp = userApp;
    }

    public UserApp getUserApp() {
        return this.userApp;
    }

    private static List<GrantedAuthority> authorities(Role roles) {
        return Collections.singletonList(new SimpleGrantedAuthority(roles.getCode()));
    }


}
