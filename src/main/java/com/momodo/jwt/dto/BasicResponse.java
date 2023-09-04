package com.momodo.jwt.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BasicResponse {

    protected final Integer status;

    public static BasicResponse of(HttpStatus status){
        return new BasicResponse(status.value());
    }
}
