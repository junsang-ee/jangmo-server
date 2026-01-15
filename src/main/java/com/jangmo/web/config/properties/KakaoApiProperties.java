package com.jangmo.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.rest.api")
public record KakaoApiProperties(
	String key,
	String uri
) {
}
