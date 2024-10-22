package com.hungdoan.aquariux.exception;

public class InvalidTrade extends RuntimeException {

    public InvalidTrade(String message) {
        super(message);
    }

    public InvalidTrade(String message, Throwable cause) {
        super(message, cause);
    }
}
