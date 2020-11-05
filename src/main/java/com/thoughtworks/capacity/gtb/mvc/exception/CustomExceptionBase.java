package com.thoughtworks.capacity.gtb.mvc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomExceptionBase extends RuntimeException {
    private final HttpStatus status;

    public CustomExceptionBase(int code, String message) {
        super(message);
        status = HttpStatus.valueOf(code);
    }
}
