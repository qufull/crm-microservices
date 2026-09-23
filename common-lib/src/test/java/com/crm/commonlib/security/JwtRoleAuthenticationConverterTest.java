package com.crm.commonlib.security;

import com.crm.commonlib.security.jwt.JwtRoleAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class JwtRoleAuthenticationConverterTest {

    private final JwtRoleAuthenticationConverter converter = new JwtRoleAuthenticationConverter();

    @Test
    void convertsRoleClaimIntoSpringSecurityAuthority() {
        Jwt jwt = Jwt.withTokenValue("token-value")
                .header("alg", "RS256")
                .claim("sub", "42")
                .claim("email", "manager@crm.dev")
                .claim("role", "MANAGER")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(900))
                .build();

        AbstractAuthenticationToken token = converter.convert(jwt);

        assertThat(token.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("ROLE_MANAGER");
    }
}