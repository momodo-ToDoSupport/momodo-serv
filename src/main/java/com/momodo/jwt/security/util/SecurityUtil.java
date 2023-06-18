package com.momodo.jwt.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * 인증 API를 제외하고는 loadUserByUsername를 호출하지 않기에 별도로 UserAdmin를 디비에서 조회해줘야 한다.
     * security context의 Authentication 객체를 이용해 adminId를 리턴해준다.
     * security context에 authentication 객체가 저장되는 시점은 JwtFilter의 doFilter 영역
     */
    public Optional<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }

}
