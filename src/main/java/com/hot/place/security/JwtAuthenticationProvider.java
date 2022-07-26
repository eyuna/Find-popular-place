package com.hot.place.security;

import com.hot.place.error.NotFoundException;
import com.hot.place.model.user.Email;
import com.hot.place.model.user.Role;
import com.hot.place.model.user.User;
import com.hot.place.service.user.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;


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

    /**
     * null 이 아닌 값을 반환하면 인증처리 완료
     * */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(authenticationToken.authenticationRequest());
    }

    private Authentication processUserAuthentication(AuthenticationRequest request) {
        try {
            User user = userService.login(new Email(request.getPrincipal()), request.getCredentials());

            JwtAuthenticationToken authenticated =
                    new JwtAuthenticationToken(
                            new JwtAuthentication(user.getSeq(), user.getEmail(), user.getName()), null, createAuthorityList(Role.USER.value())
                    );

            String apiToken = user.newApiToken(jwt, new String[]{Role.USER.value()});
            authenticated.setDetails(new AuthenticationResult(apiToken, user));
            return authenticated;
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    /**
     * 매개 변수로 받은 Authentication 객체의 구현체 클래스가 AuthenticationProvider 객체에서 사용하는 Authentication 객체와 같은 타입인지 확인
     * AuthenticationProvider 구현체는 인증을 진행할 때 인증 정보를 담은 Authentication 객체를 가지고 인증을 진행하는데
     * 이 Authentication 객체는 AuthenticationProvider 마다 다르기 때문에 support()를 통해 Authentication 객체에 맞는 AuthenticationProvider 를 찾는다.
     * */
    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
