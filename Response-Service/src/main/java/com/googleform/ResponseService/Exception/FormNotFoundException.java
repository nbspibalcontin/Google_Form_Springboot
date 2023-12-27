package com.googleform.ResponseService.Exception;

public class FormNotFoundException extends RuntimeException {

    public FormNotFoundException(String message) {
        super(message);
    }
}

