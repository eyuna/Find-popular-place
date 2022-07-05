package com.hot.place.controller;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

public class ApiResult<T> {

    private final boolean success;

    private final T response;

    private final ApiError error;

    public ApiResult(boolean success, T response, ApiError error) {
        this.success = success;
        this.response = response;
        this.error = error;
    }

    public static <T> ApiResult<T> OK(T response) { // todo generic static method
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> ERROR(Throwable throwable, HttpStatus httpStatus) {
        return new ApiResult<>(false, null, new ApiError(throwable, httpStatus));
    }

    public static ApiResult<?> ERROR(String errorMessage, HttpStatus httpStatus) {
        return new ApiResult<>(false, null, new ApiError(errorMessage, httpStatus));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("success", success)
                .append("response", response)
                .append("error", error)
                .toString();
    }
}
