package com.thoughtworks.capacity.gtb.mvc.exception;

public class UserAlreadyExistsException extends CustomExceptionBase {
    public UserAlreadyExistsException() {
        super(409, "Username already exists.");
    }
}
