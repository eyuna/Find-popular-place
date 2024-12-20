package com.hot.place.repository.user;

import com.hot.place.model.user.Email;
import com.hot.place.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(Email email);

    Optional<User> findById(Long userId);

    void update(User user);

    User insert(User user);

    List<Long> findConnectedIds(Long userId);
}
