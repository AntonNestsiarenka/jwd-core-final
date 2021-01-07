package com.epam.jwd.core_final.exception;

public class ApplicationPropertiesException extends RuntimeException {

    public ApplicationPropertiesException(String message) {
        super(message);
    }

    public ApplicationPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
