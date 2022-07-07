package com.hot.place.model.store;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class Store {

    private final Long seq;

    private final String name;

    private final String zipCode;

    private final String address;

    private final Double lat;

    private final Double lng;

    private int likes;

    private boolean likesOfMe;

    public Store(String name, String zipCode, String address) {
        this(null, name, zipCode, address, null, null, 0, false);
    }

    public Store(Long seq, String name, String zipCode, String address, Double lat, Double lng, int likes, boolean likesOfMe) {
        checkArgument(isNotEmpty(name), "name must be provided.");
        checkArgument(
                name.length() >= 1 && name.length() <= 50,
                "name length must be between 1 and 50 characters."
        );
        checkArgument(isNotEmpty(zipCode), "zipCode must be provided.");
        checkArgument(
                zipCode.length() == 5,
                "zip code length must be 5 characters."
        );
        checkArgument(isNotEmpty(address), "address must be provided.");
        checkArgument(
                address.length() >= 1 && address.length() <= 500,
                "address length must be between 1 and 500 characters."
        );

        this.seq = seq;
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.likes = likes;
        this.likesOfMe = likesOfMe;
    }

    public int incrementAndGetLikes() {
        likesOfMe = true;
        return likes++;
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getAddress() {
        return address;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isLikesOfMe() {
        return likesOfMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store store = (Store) o;
        return Objects.equals(store.seq, seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("name", name)
                .append("zipCode", zipCode)
                .append("address", address)
                .append("lat", lat)
                .append("lng", lng)
                .append("likes", likes)
                .append("likesOfMe", likesOfMe)
                .toString();
    }

    static public class Builder {
        private Long seq;
        private String name;
        private String zipCode;
        private String address;
        private Double lat;
        private Double lng;
        private int likes;
        private boolean likesOfMe;

        public Builder() {}

        public Builder(Store store) {
            this.seq = store.seq;
            this.name = store.name;
            this.zipCode = store.zipCode;
            this.address = store.address;
            this.lat = store.lat;
            this.lng = store.lng;
            this.likes = store.likes;
            this.likesOfMe = store.likesOfMe;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder lat(Double lat) {
            this.lat = lat;
            return this;
        }

        public Builder lng(Double lng) {
            this.lng = lng;
            return this;
        }

        public Builder likes(int likes) {
            this.likes = likes;
            return this;
        }

        public Builder likesOfMe(boolean likesOfMe) {
            this.likesOfMe = likesOfMe;
            return this;
        }

        public Store build() {
            return new Store(seq, name, zipCode, address, lat, lng, likes, likesOfMe);
        }
    }
}
