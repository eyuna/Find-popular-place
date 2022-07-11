package com.hot.place.repository.store;

import com.hot.place.model.store.Store;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class JdbcStoreRepository implements StoreRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStoreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Store> findBySeq(Long seq) {
        return null;
//        return jdbcTemplate.query("select * from stores ")
    }

    @Override
    public Optional<Store> findByNameAndZipCode(String name, String zipCode) {
        return Optional.empty();
    }
}
