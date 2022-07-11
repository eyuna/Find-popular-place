package com.hot.place.service.store;

import com.hot.place.model.store.Store;
import com.hot.place.repository.store.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Store> findBySeq(Long seq) {
        return storeRepository.findBySeq(seq);
    }

    @Transactional(readOnly = true)
    public Optional<Store> findByNameAndZipcode(String name, String zipCode) {
        return storeRepository.findByNameAndZipCode(name, zipCode);
    }
}
