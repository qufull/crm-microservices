package com.crm.commonlib.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticatedUserFactoryTest {

    @Test
    void extractsUserIdEmailAndRoleFromJwtClaims() {
        Jwt jwt = Jwt.withTokenValue("token-value")
                .header("alg", "RS256")
                .claim("sub", "42")
                .claim("email", "manager@crm.dev")
                .claim("role", "MANAGER")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(900))
                .build();

        AuthenticatedUser user = AuthenticatedUserFactory.from(jwt);

        assertThat(user.userId()).isEqualTo(42L);
        assertThat(user.email()).isEqualTo("manager@crm.dev");
        assertThat(user.role()).isEqualTo("MANAGER");
    }
}