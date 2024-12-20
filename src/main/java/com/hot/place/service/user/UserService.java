package com.hot.place.service.user;

import com.hot.place.error.NotFoundException;
import com.hot.place.model.user.Email;
import com.hot.place.model.user.User;
import com.hot.place.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public User join(String name, Email email, String password) {
        checkArgument(isNotEmpty(password), "password must be provided.");
        checkArgument(
                password.length() >= 4 && password.length() <= 15,
                "password length must be between 4 and 15 characters."
        );

        User user = new User(email, name, passwordEncoder.encode(password));
        return insert(user);
    }

    @Transactional
    public User updateProfileImage(Long userId, String profileImageUrl) {
        User user = findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));
        user.updateProfileImage(profileImageUrl);
        update(user);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        checkArgument(email != null, "email must be provided.");
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User login(Email email, String password) {
        checkArgument(isNotEmpty(password), "password must be provided.");

        User user = findByEmail(email).orElseThrow(() -> new NotFoundException(User.class, email));
        user.login(passwordEncoder, password);
        user.afterLoginSuccess();
        update(user);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {
        checkArgument(userId != null, "userId must be provided.");
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> findConnectedIds(Long userId) {
        checkArgument(userId != null, "userId must be provided.");

        return userRepository.findConnectedIds(userId);
    }

    private void update(User user) {
        userRepository.update(user);
    }

    private User insert(User user) {
        return userRepository.insert(user);
    }
}

