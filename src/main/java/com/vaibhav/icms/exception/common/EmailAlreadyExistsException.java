package com.vaibhav.icms.exception.common;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message){
        super(message);
    }
    
}
