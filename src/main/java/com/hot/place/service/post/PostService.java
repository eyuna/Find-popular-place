package com.hot.place.service.post;

import com.hot.place.model.post.Post;
import com.hot.place.repository.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post write(Post post) {
        return insert(post);
    }

    @Transactional
    public Post modify(Post post) {
        update(post);
        return post;
    }

    private Post insert(Post post) {
        return postRepository.insert(post);
    }

    private void update(Post post) {
        postRepository.update(post);
    }
}
