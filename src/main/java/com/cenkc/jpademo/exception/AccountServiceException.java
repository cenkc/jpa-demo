package com.cenkc.jpademo.exception;

/**
 * created by cenkc on 12/31/2018
 */
public class AccountServiceException extends RuntimeException {

    public AccountServiceException(String message) {
        super(message);
    }

    public AccountServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
