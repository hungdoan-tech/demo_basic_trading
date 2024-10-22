package com.hungdoan.aquariux.exception;

public class OptimisticLockingFailureException extends Exception {

    public OptimisticLockingFailureException(String message) {
        super(message);
    }
}
