package com.momodo.jwt.exception.error;


import com.momodo.jwt.exception.ErrorCode;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }
}
