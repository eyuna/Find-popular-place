package com.hot.place.service.store;

import com.hot.place.model.store.Store;
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
class StoreServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private StoreService storeService;

    private Long seq;

    private String name;

    private String zipCode;

    @BeforeAll
    void setup() {
        seq = 2L;
        name = "test store 1";
        zipCode = "12345";
    }

    @Test
    @Order(1)
    void 시퀀스로_가게정보를_가져온다() {
        Store store = storeService.findBySeq(seq).orElse(null);
        assertThat(store, is(notNullValue()));
        assertThat(store.getSeq(), is(seq));
        log.info(store.toString());
    }

    @Test
    @Order(2)
    void 이름_우편번호로_가게정보를_가져온다() {
        // todo 이름 공백
        Store store = storeService.findByNameAndZipcode(name, zipCode).orElse(null);
        assertThat(store, is(notNullValue()));
        assertThat(store.getName(), is(name));
        assertThat(store.getZipCode(), is(zipCode));
        log.info(store.toString());
    }
}