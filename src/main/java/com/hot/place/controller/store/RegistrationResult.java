package com.hot.place.controller.store;

public class RegistrationResult {

    private StoreDto storeDto;

    public RegistrationResult(StoreDto storeDto) {
        this.storeDto = storeDto;
    }

    public StoreDto getStoreDto() {
        return storeDto;
    }

    @Override
    public String toString() {
        return "RegistrationResult{" +
                "storeDto=" + storeDto +
                '}';
    }
}
