package com.hot.place.repository.store;

import com.hot.place.model.store.Store;

import java.util.Optional;

public interface StoreRepository {

    Optional<Store> findBySeq(Long seq);

    Optional<Store> findByNameAndZipCode(String name, String zipCode);
}
