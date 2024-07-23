package com.example.demo_mhdigital.config.exception;

import lombok.Data;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Data
public class ApiException extends Exception {
    static final long serialVersionUID = 1651914954615L;
    private final int code;

    public ApiException(String message) {
        super(message);
        this.code = BAD_REQUEST.value();
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.code = BAD_REQUEST.value();
    }

    public ApiException(int statusCode, String s) {
        super(s);
        this.code = statusCode;
    }

    public ApiException(int statusCode, String s, Throwable cause) {
        super(s, cause);
        this.code = statusCode;
    }
}
