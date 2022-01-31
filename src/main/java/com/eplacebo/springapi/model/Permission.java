package com.eplacebo.springapi.model;

public enum Permission {
    USERS_READ("user:read"),
    USERS_WRITE("user:write"),
    USERS_FULL_READ("user:full_read"),
    USERS_DOWNLOAD("user:download"),
    USERS_UPLOAD("user:upload");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}