package com.hot.place.repository.user;

import com.hot.place.model.user.Email;
import com.hot.place.model.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hot.place.util.DateTimeUtils.dateTimeOf;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        List<User> results = jdbcTemplate.query("select * from users where email = ?", userRowMapper, email.getAddress());
        return Optional.ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET name=?,passwd=?,login_count=?,last_login_at=? WHERE seq=?",
                user.getName(),
                user.getPassword(),
                user.getLoginCount(),
                user.getLastLoginAt().orElse(null),
                user.getSeq()
        );
    }

    @Override
    public List<Long> findConnectedIds(Long userId) {
        return jdbcTemplate.query(
                "SELECT target_seq FROM connections WHERE user_seq=? AND granted_at is not null ORDER BY target_seq",
                (rs, rowNum) -> rs.getLong("target_seq"),
                userId
        );
    }

    static RowMapper<User> userRowMapper = (rs, rowNum) -> new User.Builder()
            .seq(rs.getLong("seq"))
            .email(new Email(rs.getString("email")))
            .name(rs.getString("name"))
            .password(rs.getString("passwd"))
            .profileImageUrl(rs.getString("profile_image_url"))
            .loginCount(rs.getInt("login_count"))
            .lastLoginAt(dateTimeOf(rs.getTimestamp("last_login_at")))
            .createAt(dateTimeOf(rs.getTimestamp("create_at")))
            .build();
}
