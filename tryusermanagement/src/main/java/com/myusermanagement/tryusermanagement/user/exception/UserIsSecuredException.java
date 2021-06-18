package com.myusermanagement.tryusermanagement.user.exception;

public class UserIsSecuredException extends RuntimeException{
    public UserIsSecuredException(String message) {
        super(message);
    }
}
