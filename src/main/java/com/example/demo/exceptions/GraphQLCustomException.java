package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class GraphQLCustomException extends RuntimeException {

    private final HttpStatus status;

    public GraphQLCustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
