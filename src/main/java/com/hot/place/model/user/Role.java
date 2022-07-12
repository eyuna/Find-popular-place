package com.hot.place.model.user;

public enum Role {

    USER("ROLE_USER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
