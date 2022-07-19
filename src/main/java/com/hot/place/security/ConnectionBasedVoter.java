package com.hot.place.security;

import com.hot.place.service.user.UserService;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.test.web.client.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class ConnectionBasedVoter implements AccessDecisionVoter<FilterInvocation> {
    // Invocation 에 체크할 대상이 담겨 있다.
    // 그 내용이 ConfigAttribute 에 들어가고 AccessDecisionVoter 가 vote 를 통해 검사한다.

    private final RequestMatcher requiresAuthorizationRequestMatcher; // 전략 패턴

    private final Function<String, Long> idExtractor;
    // 1개의 인자(Type T)를 받고 1개의 객체(Type R)를 리턴하는 함수형 인터페이스

    private UserService userService;

    public ConnectionBasedVoter(RequestMatcher requiresAuthorizationRequestMatcher, Function<String, Long> idExtractor) {
        checkArgument(requiresAuthorizationRequestMatcher != null, "requiresAuthorizationRequestMatcher must be provided.");
        checkArgument(idExtractor != null, "idExtractor must be provided.");

        this.requiresAuthorizationRequestMatcher = requiresAuthorizationRequestMatcher;
        this.idExtractor = idExtractor;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        HttpServletRequest request = fi.getRequest();

        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

}