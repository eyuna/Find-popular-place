package com.hot.place.service.store;

import com.hot.place.model.store.Store;
import com.hot.place.repository.store.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

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
        checkArgument(zipCode.length() == 5, "zip code length must be 5 characters.");

        return storeRepository.findByNameAndZipCode(name, zipCode);
    }
}
