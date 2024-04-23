package com.jpashop.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private HttpStatus httpStatus;
    private int code;
    private String message;
    private T data;

    private ApiResponse(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return of(httpStatus, httpStatus.name(), data);
    }

    public static <T> ApiResponse<T> of(T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }
}

