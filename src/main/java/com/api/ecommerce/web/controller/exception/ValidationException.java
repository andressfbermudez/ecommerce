package com.api.ecommerce.web.controller.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
