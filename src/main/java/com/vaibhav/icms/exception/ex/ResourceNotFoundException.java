package com.vaibhav.icms.exception.ex;

// using this for missing projects, tasks or users
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
