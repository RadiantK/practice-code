package jpabook.jpashop.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private HttpStatus status;
    private T Data;

    public ApiResponse(HttpStatus status, T data) {
        this.code = status.value();
        this.status = status;
        Data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return new ApiResponse<>(httpStatus, data);
    }

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(HttpStatus.OK, data);
    }

}
