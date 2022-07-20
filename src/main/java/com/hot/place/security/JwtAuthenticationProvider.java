package com.hot.place.security;

import com.hot.place.service.user.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * 인증 제공자
 * AuthenticationProvider 는 인증을 검사 할 Authentication 대상을 알려주는 supports()를 지원한다.
 * supports()에 자신이 담당하는 인증토큰 방식을 정의하여 내가 담당한 인증토큰을 찾는다.
 * authenticate()에서는 Authentication 을 입력값과 출력값으로 사용하여 검증한다.
 * */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;

    private final UserService userService;

    public JwtAuthenticationProvider(Jwt jwt, UserService userService) {
        this.jwt = jwt;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // todo
        // 같은 타입인지 확인
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
