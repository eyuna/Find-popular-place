package com.hot.place.model.post;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

public class Post {

    private final Long seq;

    private final Long storeId;

    private final Long userId;

    private String contents;

    private int likes;

    private boolean likesOfMe;

    private final LocalDateTime createAt;

    public Post(Long storeId, Long userId, String contents) {
        this(null, storeId, userId, contents, 0, false, null);
    }

    public Post(Long seq, Long storeId, Long userId, String contents, int likes, boolean likesOfMe, LocalDateTime createAt) {
        checkArgument(storeId != null, "Store ID must be provided.");
        checkArgument(userId != null, "User ID must be provided.");
        checkArgument(isNotEmpty(contents), "Contents must be provided.");
        checkArgument(
                contents.length() >= 4 && contents.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.seq = seq;
        this.storeId = storeId;
        this.userId = userId;
        this.contents = contents;
        this.likes = likes;
        this.likesOfMe = likesOfMe;
        this.createAt = defaultIfNull(createAt, now());
    }

    public void modify(String contents) {
        checkArgument(StringUtils.isNotEmpty(contents), "contents must be provided.");
        checkArgument(
                contents.length() >= 4 && contents.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.contents = contents;
    }

    public int incrementAndGetLikes() {
        likesOfMe = true;
        return ++likes;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getStoreId() {
        return storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContents() {
        return contents;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isLikesOfMe() {
        return likesOfMe;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;
        return Objects.equals(seq, post.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("storeId", storeId)
                .append("userId", userId)
                .append("contents", contents)
                .append("likes", likes)
                .append("likesOfMe", likesOfMe)
                .append("createAt", createAt)
                .toString();
    }

    static public class Builder {
        private Long seq;
        private Long storeId;
        private Long userId;
        private String contents;
        private int likes;
        private boolean likesOfMe;
        private LocalDateTime createAt;

        public Builder() {
        }

        public Builder(Post post) {
            this.seq = post.seq;
            this.storeId = post.storeId;
            this.userId = post.userId;
            this.contents = post.contents;
            this.likes = post.likes;
            this.likesOfMe = post.likesOfMe;
            this.createAt = post.createAt;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder likes(int likes) {
            this.likes = likes;
            return this;
        }

        public Builder likesOfMe(boolean likesOfMe) {
            this.likesOfMe = likesOfMe;
            return this;
        }

        public Builder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public Post build() {
            return new Post(seq, storeId, userId, contents, likes, likesOfMe, createAt);
        }
    }
}
