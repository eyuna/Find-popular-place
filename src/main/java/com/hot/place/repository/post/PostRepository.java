package com.hot.place.repository.post;

import com.hot.place.model.post.Post;

public interface PostRepository {

    Post insert(Post post);

    void update(Post post);
}
