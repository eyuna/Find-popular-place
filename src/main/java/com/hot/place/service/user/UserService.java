package com.hot.place.service.user;

import com.hot.place.model.user.Email;
import com.hot.place.model.user.User;
import com.hot.place.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        checkArgument(email != null, "email must be provided.");
        return userRepository.findByEmail(email);
    }
}

