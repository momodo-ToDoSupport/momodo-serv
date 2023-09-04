package com.momodo.jwt.dto;

import com.momodo.jwt.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse extends BasicResponse{
    private String code;
    private String message;

    private ErrorResponse(Integer status, String code, String message) {
        super(status);
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of (ErrorCode errorCode){
        return new ErrorResponse(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
    }

    public static ErrorResponse of (Integer status, String code, String message){
        return new ErrorResponse(status, code, message);
    }
}
