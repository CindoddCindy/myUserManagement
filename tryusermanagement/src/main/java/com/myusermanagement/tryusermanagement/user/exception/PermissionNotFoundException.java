package com.myusermanagement.tryusermanagement.user.exception;
public class PermissionNotFoundException extends RuntimeException{

    public PermissionNotFoundException(String message) {
        super(message);
    }
}
