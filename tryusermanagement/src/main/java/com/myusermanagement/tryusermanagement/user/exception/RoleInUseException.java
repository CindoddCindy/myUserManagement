package com.myusermanagement.tryusermanagement.user.exception;

public class RoleInUseException extends RuntimeException{

    public RoleInUseException(String message) {
        super(message);
    }
}
