package com.hot.place.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 인증 주체
 * 로그인 전에는 String 타입이고, 로그인 후에는 {@link JwtAuthentication} 타입이다.
 * */
public class JwtAuthenticationToken extends AbstractAuthenticationToken { //todo

    private final Object principal;

    private String credentials;

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, String credentials) {
        super(null);
        super.setAuthenticated(false);

        this.principal = principal;
        this.credentials = credentials;
    }

    JwtAuthenticationToken(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        super.setAuthenticated(authenticated);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
