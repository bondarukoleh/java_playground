package com.oleh.rest.littleRESTApp.Utils.exceptionHandle;

public class GreetingNotValidException extends RuntimeException {
    public GreetingNotValidException(String message) {
        super(message);
    }
}
