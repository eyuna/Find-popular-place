package com.hot.place.repository.post;

import com.hot.place.model.post.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

import static com.hot.place.util.DateTimeUtils.dateTimeOf;
import static com.hot.place.util.DateTimeUtils.timestampOf;

@Repository
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post insert(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO posts(store_seq,user_seq,contents,like_count,create_at) VALUES (?,?,?,?,?)"
                    , new String[]{"seq"});
            ps.setLong(1, post.getStoreId());
            ps.setLong(2, post.getUserId());
            ps.setString(3, post.getContents());
            ps.setInt(4, post.getLikes());
            ps.setTimestamp(5, timestampOf(post.getCreateAt()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        long generatedSeq = key != null ? key.longValue() : -1;
        return new Post.Builder(post)
                .seq(generatedSeq)
                .build();
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                "UPDATE posts SET contents=?,like_count=?,comment_count=? WHERE seq=?",
                post.getContents(),
                post.getLikes(),
                post.getSeq()
        );
    }

    @Override
    public List<Post> findAll(Long writerId, Long userId, long offset, int limit) {
        return jdbcTemplate.query(
                "SELECT p.*,u.email,u.name," +
                        "ifnull(select 'true' from likes l where l.post_seq = p.seq and l.user_seq = ?, false) as likes_of_me " +
                        "FROM posts p JOIN users u ON p.user_seq=u.seq " +
                        "WHERE p.user_seq = ? ORDER BY p.seq DESC " +
                        "LIMIT ?,? ",
                mapper,
                userId, writerId, offset, limit
        );
    }

    static RowMapper<Post> mapper = (rs, rowNum) -> new Post.Builder()
            .seq(rs.getLong("seq"))
            .storeId(rs.getLong("store_seq"))
            .userId(rs.getLong("user_seq"))
            .contents(rs.getString("contents"))
            .likes(rs.getInt("like_count"))
            .likesOfMe(rs.getBoolean("likes_of_me"))
            .createAt(dateTimeOf(rs.getTimestamp("create_at")))
            .build();
}
