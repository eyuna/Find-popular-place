package com.hot.place.repository.user;

import com.hot.place.model.user.Email;
import com.hot.place.model.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static com.hot.place.util.DateTimeUtils.dateTimeOf;
import static com.hot.place.util.DateTimeUtils.timestampOf;
import static java.util.Optional.ofNullable;

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
                "UPDATE users SET name=?,passwd=?,profile_image_url=?,login_count=?,last_login_at=? WHERE seq=?",
                user.getName(),
                user.getPassword(),
                user.getProfileImageUrl().orElse(null),
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

    @Override
    public Optional<User> findById(Long userId) {
        List<User> results = jdbcTemplate.query("select * from users where seq = ?", userRowMapper, userId);
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public User insert(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(name,email,passwd,profile_image_url,login_count,last_login_at,create_at) VALUES (?,?,?,?,?,?,?)", new String[]{"seq"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail().getAddress());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getProfileImageUrl().orElse(null));
            ps.setInt(5, user.getLoginCount());
            ps.setTimestamp(6, timestampOf(user.getLastLoginAt().orElse(null)));
            ps.setTimestamp(7, timestampOf(user.getCreateAt()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        long generatedSeq = key != null ? key.longValue() : -1;
        return new User.Builder(user)
                .seq(generatedSeq)
                .build();
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
