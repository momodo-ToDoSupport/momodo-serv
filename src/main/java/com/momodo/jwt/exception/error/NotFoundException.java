package com.momodo.jwt.exception.error;

public class NotFoundException extends RuntimeException{

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
