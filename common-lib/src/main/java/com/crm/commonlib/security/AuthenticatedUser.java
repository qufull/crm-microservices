package com.crm.commonlib.security;

public record AuthenticatedUser(Long userId, String email, String role) {

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}