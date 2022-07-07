package com.hot.place.controller.store;

import com.hot.place.model.store.Store;
import io.swagger.annotations.ApiModelProperty;

import static org.springframework.beans.BeanUtils.copyProperties;

public class StoreDto {

    @ApiModelProperty(value = "PK", required = true)
    private Long seq;

    private String name;

    private String zipCode;

    private String address;

    private Double lat;

    private Double lng;

    private int likes;

    private boolean likesOfMe;

    public StoreDto(Store source) {
        copyProperties(source, this);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLikesOfMe() {
        return likesOfMe;
    }

    public void setLikesOfMe(boolean likesOfMe) {
        this.likesOfMe = likesOfMe;
    }

    @Override
    public String toString() {
        return "StoreDto{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", likes=" + likes +
                ", likesOfMe=" + likesOfMe +
                '}';
    }
}
