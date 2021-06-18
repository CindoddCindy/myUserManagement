package com.myusermanagement.tryusermanagement.user.exception;

public class PermissionInUseException  extends RuntimeException {
    public PermissionInUseException(String message) {
        super(message);
    }
}
