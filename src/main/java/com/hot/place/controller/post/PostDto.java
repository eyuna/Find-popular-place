package com.hot.place.controller.post;

import com.hot.place.model.post.Post;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

public class PostDto {

    private Long seq;

    private Long storeId;

    private Long userId;

    private String contents;

    private int likes;

    private boolean likesOfMe;

    private LocalDateTime createAt;

    public PostDto(Post source) {
        copyProperties(source, this);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLikesOfMe() {
        return likesOfMe;
    }

    public void setLikesOfMe(boolean likesOfMe) {
        this.likesOfMe = likesOfMe;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "seq=" + seq +
                ", storeSeq=" + storeId +
                ", userSeq=" + userId +
                ", contents='" + contents + '\'' +
                ", likes=" + likes +
                ", likesOfMe=" + likesOfMe +
                ", createAt=" + createAt +
                '}';
    }
}
