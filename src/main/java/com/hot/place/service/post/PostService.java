package com.hot.place.service.post;

import com.hot.place.model.post.Post;
import com.hot.place.repository.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

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

    @Transactional(readOnly = true)
    public List<Post> findAll(Long writerId, Long userId, long offset, int limit) {
        checkArgument(writerId != null, "writerId must be provided.");
        checkArgument(userId != null, "user id must be provided.");

        return postRepository.findAll(
                writerId,
                userId,
                checkOffset(offset),
                checkLimit(limit)
        );
    }

    private long checkOffset(long offset) {
        return offset < 0 ? 0 : offset;
    }

    private int checkLimit(int limit) {
        return (limit < 1 || limit > 5) ? 5 : limit;
    }

    private Post insert(Post post) {
        return postRepository.insert(post);
    }

    private void update(Post post) {
        postRepository.update(post);
    }
}
