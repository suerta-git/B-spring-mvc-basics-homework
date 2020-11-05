package com.thoughtworks.capacity.gtb.mvc.exception;

public class PasswordNotCorrectException extends CustomExceptionBase {
    public PasswordNotCorrectException() {
        super(400, "Password not correct.");
    }
}
