package com.jangmo.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sms.api")
public record SmsProperties(
        String key,
        String secret,
        String sender,
        String authContent,
        String mercenaryContent
) {
}
