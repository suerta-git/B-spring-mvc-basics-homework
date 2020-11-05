package com.thoughtworks.capacity.gtb.mvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> notValidHandler(MethodArgumentNotValidException e) {
        final String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest().body(new CustomError(400, message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomError> illegalArgumentHandler(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new CustomError(400, e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomError> ConstraintViolationHandler(ConstraintViolationException e) {
        String message = "Validation error.";
        final Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (!Objects.isNull(constraintViolations) && !constraintViolations.isEmpty()) {
            message = constraintViolations.iterator().next().getMessage();
        }
        CustomError errorResult = new CustomError(400, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }

    @ExceptionHandler(CustomExceptionBase.class)
    public ResponseEntity<CustomError> CustomExceptionHandler(CustomExceptionBase e) {
        final CustomError error = new CustomError(e.getStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(error);
    }
}
