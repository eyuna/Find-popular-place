package com.hot.place.configure.support;

/**
 * LIMIT: 행을 얼마나 가져올지
 * OFFSET: 어디서 부터 가져올지
 */
public interface Pageable {

    long offset();

    int limit();
}
