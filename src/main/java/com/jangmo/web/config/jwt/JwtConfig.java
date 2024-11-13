package com.jangmo.web.config.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "jangmo.jwt")
@Component
public class JwtConfig {
    private String secret;

    private Duration validTime;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(getSecret()));
    }
}
