package com.hot.place.repository.user;

import com.hot.place.model.user.Email;
import com.hot.place.model.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(Email email);
}
