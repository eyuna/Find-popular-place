package com.hot.place.configure.support;

public class SimpleOffsetPageRequest implements Pageable {

    private final long offset;

    private final int limit;

    public SimpleOffsetPageRequest() {
        this(0, 5);
    }

    public SimpleOffsetPageRequest(long offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public long offset() {
        return offset;
    }

    @Override
    public int limit() {
        return limit;
    }

    @Override
    public String toString() {
        return "SimpleOffsetPageRequest{" +
                "offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
