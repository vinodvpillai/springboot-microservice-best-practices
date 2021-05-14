package com.vinod.microservices.best.practices.exception;

/**
 * Customer not found exception class.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }
}
