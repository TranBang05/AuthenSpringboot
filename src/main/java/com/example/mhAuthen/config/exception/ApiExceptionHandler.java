package com.example.demo_mhdigital.config.exception;

import com.example.demo_mhdigital.data.response.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage TodoException(Exception ex, WebRequest request) {
        return new ErrorMessage(400, ex.getMessage());
    }
}
