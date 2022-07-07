package com.hot.place.service.user;

import com.hot.place.model.user.Email;
import com.hot.place.model.user.User;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    private Email email;

    @BeforeAll
    void setup() {
        email = new Email("test00@gmail.com");
    }

    @Test
    @Order(1)
    void 내정보를_가져온다() {
        User user  = userService.findByEmail(email).orElse(null);
        assertThat(user, is(notNullValue()));
        log.info(user.toString());
    }
}