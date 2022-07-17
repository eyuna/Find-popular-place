package com.hot.place.security;

public class AuthenticationRequest {

    private String principal;

    private String credentials;

    public AuthenticationRequest(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "principal='" + principal + '\'' +
                ", credentials='" + credentials + '\'' +
                '}';
    }
}
