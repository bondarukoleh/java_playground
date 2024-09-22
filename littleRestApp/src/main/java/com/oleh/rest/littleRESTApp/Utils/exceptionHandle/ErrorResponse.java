package com.oleh.rest.littleRESTApp.Utils.exceptionHandle;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String details;

    public ErrorResponse(HttpStatus status, String message, String details) {
        this.timestamp = new Date();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.details = details;
    }
}