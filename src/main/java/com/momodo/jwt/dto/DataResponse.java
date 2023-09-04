package com.momodo.jwt.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataResponse<T> extends BasicResponse{
    private final T data;

    public DataResponse(T data) {
        super(HttpStatus.OK.value());
        this.data = data;
    }

    public static <T> DataResponse<T> of(T data) {
        return new DataResponse<>(data);
    }
}
