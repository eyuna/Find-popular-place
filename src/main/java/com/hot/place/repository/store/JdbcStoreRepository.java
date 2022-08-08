package com.hot.place.repository.store;

import com.hot.place.model.store.Store;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcStoreRepository implements StoreRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStoreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Store insert(Store store) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO stores(name,zip_code,address,user_seq) VALUES (?,?,?,?)", new String[]{"seq"});
            ps.setString(1, store.getName());
            ps.setString(2, store.getZipCode());
            ps.setString(3, store.getAddress());
            ps.setLong(4, store.getUserSeq());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        long generatedSeq = key != null ? key.longValue() : -1;
        return new Store.Builder(store)
                .seq(generatedSeq)
                .build();
    }

    @Override
    public Optional<Store> findBySeq(Long seq) {
        List<Store> results = jdbcTemplate.query("select * from stores where seq = ? ", storeRowMapper, seq);
        return Optional.ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public Optional<Store> findByNameAndZipCode(String name, String zipCode) {
        List<Store> results = jdbcTemplate.query("select * from stores where name = ? and zip_code = ? "
                , storeRowMapper, name, zipCode);
        return Optional.ofNullable(results.isEmpty() ? null : results.get(0));
    }

    static RowMapper<Store> storeRowMapper = (rs, rowNum) -> new Store.Builder()
            .seq(rs.getLong("seq"))
            .name(rs.getString("name"))
            .zipCode(rs.getString("zip_code"))
            .address(rs.getString("address"))
            .lat(rs.getDouble("lat"))
            .lng(rs.getDouble("lng"))
            .likes(rs.getInt("likes"))
            .userSeq(rs.getLong("user_seq"))
            .build();
}
