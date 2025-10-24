package com.jangmo.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jangmo.admin")
public record AdminProperties(
        String name,
        String mobile,
        String password
) {
}
