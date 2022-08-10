package com.hot.place.repository.post;

import com.hot.place.model.post.Post;

import java.util.List;

public interface PostRepository {

    Post insert(Post post);

    void update(Post post);

    List<Post> findAll(Long writerId, Long userId, long offset, int limit);
}
