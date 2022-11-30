package com.revature.utils.custom_exceptions;

public class InvalidAuthException extends RuntimeException{
    public InvalidAuthException() {
        super();
    }

    public InvalidAuthException(String message) {
        super(message);
    }

    public InvalidAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthException(Throwable cause) {
        super(cause);
    }

    protected InvalidAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
