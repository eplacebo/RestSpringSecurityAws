package com.eplacebo.springapi.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.USERS_READ)),
    MODERATOR(Set.of(Permission.USERS_READ, Permission.USERS_DOWNLOAD, Permission.USERS_UPLOAD)),
    ADMIN(Set.of(Permission.USERS_READ, Permission.USERS_WRITE, Permission.USERS_DOWNLOAD, Permission.USERS_UPLOAD, Permission.USERS_FULL_READ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}