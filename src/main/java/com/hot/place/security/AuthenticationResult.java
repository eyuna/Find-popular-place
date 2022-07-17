package com.hot.place.security;

import com.hot.place.model.user.User;

import static com.google.common.base.Preconditions.checkArgument;

public class AuthenticationResult {

    private final String apiToken;

    private final User user;

    public AuthenticationResult(String apiToken, User user) {
        checkArgument(apiToken != null, "apiToken must be provided.");
        checkArgument(user != null, "user must be provided.");

        this.apiToken = apiToken;
        this.user = user;
    }

    public String getApiToken() {
        return apiToken;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "AuthenticationResult{" +
                "apiToken='" + apiToken + '\'' +
                ", user=" + user +
                '}';
    }
}
