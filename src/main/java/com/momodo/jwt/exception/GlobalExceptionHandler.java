package com.momodo.jwt.exception;

import com.momodo.jwt.dto.BasicResponse;
import com.momodo.jwt.dto.ErrorResponse;
import com.momodo.jwt.exception.error.DuplicateMemberException;
import com.momodo.jwt.exception.error.InvalidRefreshTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Bean Validation에 실패했을 때, 에러메시지를 내보내기 위한 Exception Handler
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleParamViolationException(BindException ex) {
        //파라미터 validation에 걸렸을 경우
        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_FAILED;

        ErrorResponse response = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(DuplicateMemberException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateMemberException(DuplicateMemberException ex) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_MEMBER_EXCEPTION;

        ErrorResponse response = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_REFRESH_TOKEN;

        ErrorResponse response = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

}
