package com.hot.place.controller.post;

import com.hot.place.model.post.Post;

public class PostingRequest {

    private Long storeId;

    private String contents;

    private PostingRequest() {}

    public String getContents() {
        return contents;
    }

    public Post newPost(Long userId) {
        return new Post(storeId, userId, contents);
    }

    @Override
    public String toString() {
        return "PostingRequest{" +
                "contents='" + contents + '\'' +
                '}';
    }
}
