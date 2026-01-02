package com.vaibhav.icms.exception.ex;

public class UserAlreadyAssignedException extends RuntimeException {
    public UserAlreadyAssignedException(String message) {
        super(message);
    }
}
