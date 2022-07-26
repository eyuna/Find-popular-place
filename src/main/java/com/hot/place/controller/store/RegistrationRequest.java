package com.hot.place.controller.store;

import io.swagger.annotations.ApiModelProperty;

public class RegistrationRequest {

    @ApiModelProperty(value = "가게 이름", required = true)
    private String name;

    @ApiModelProperty(value = "우편번호", required = true)
    private String zipCode;

    @ApiModelProperty(value = "주소", required = true)
    private String address;

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

    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
