package com.crm.authservice.security;

import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RsaKeyManager {

    private final RSAKey rsaKey;

    public RsaKeyManager() {
        try {
            // Генерируем RSA ключ на 2048 бит
            this.rsaKey = new RSAKeyGenerator(2048)
                    .keyUse(KeyUse.SIGNATURE)
                    .keyID(UUID.randomUUID().toString())
                    .generate();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate RSA key pair", e);
        }
    }

    public RSAKey getRsaKey() {
        return rsaKey;
    }
}