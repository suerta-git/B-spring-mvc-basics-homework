package com.thoughtworks.capacity.gtb.mvc.exception;

public class UserNotExistsException extends CustomExceptionBase {
    public UserNotExistsException() {
        super(404, "User not exists.");
    }
}
