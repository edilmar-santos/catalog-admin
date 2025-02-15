package com.eddy.admin.catalog.domain.exceptions;

public class NoStackTraceException extends RuntimeException {

    NoStackTraceException(String message) {
        super(message, null);
    }

    NoStackTraceException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
