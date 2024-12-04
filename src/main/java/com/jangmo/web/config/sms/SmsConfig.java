package com.jangmo.web.config.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "spring.sms.api")
@Component
public class SmsConfig {

    private String key;

    private String secret;

    private String sender;

    private String content;
}
