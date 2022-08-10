package com.hot.place.repository.post;

import com.hot.place.model.post.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

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
}
