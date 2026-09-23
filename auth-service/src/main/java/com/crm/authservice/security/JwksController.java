package com.crm.authservice.security;

import com.nimbusds.jose.jwk.JWKSet;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwksController {
    private final RsaKeyManager keyManager;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {

        JWKSet jwkSet = new JWKSet(keyManager.getRsaKey());
        return jwkSet.toJSONObject();
    }
}
