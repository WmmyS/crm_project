package com.wesley.crm.exceptions;

import org.springframework.http.HttpStatus;

public class CrmException extends RuntimeException {

    private HttpStatus status;

    public CrmException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CrmException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CrmException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CrmException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}