package com.crm.commonlib.security.jwt;

import org.springframework.security.oauth2.jwt.Jwt;

public final class AuthenticatedUserFactory {

    private AuthenticatedUserFactory() {
    }

    public static AuthenticatedUser from(Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        String email = jwt.getClaimAsString("email");
        String role = jwt.getClaimAsString("role");
        return new AuthenticatedUser(userId, email, role);
    }
}