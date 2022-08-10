package com.hot.place.service.post;

import com.hot.place.model.post.Post;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostService postService;

    private Long writerId;

    private Long storeId;

    private Long userId;

    @BeforeAll
    void setup() {
        writerId = 2L;
        storeId = 1L;
        userId = 1L;
    }

    @Test
    @Order(1)
    void 포스트를_작성한다() {
        String contents = randomAlphabetic(40);
        Post post = postService.write(new Post(storeId, writerId, contents));
        assertThat(post, is(notNullValue()));
        assertThat(post.getSeq(), is(notNullValue()));
        assertThat(post.getContents(), is(contents));
        log.info("Written post: {}", post);
    }

    @Test
    @Order(2)
    void 포스트_목록을_조회한다() {
        List<Post> posts = postService.findAll(writerId, userId, 0, 20);
        assertThat(posts, is(notNullValue()));
        posts.forEach(post -> log.info(post.toString()));
        assertThat(posts.size(), is(2));
    }
}