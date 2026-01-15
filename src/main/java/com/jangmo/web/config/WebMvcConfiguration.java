package com.jangmo.web.config;

import com.jangmo.web.config.converter.StringToInstantConverter;
import com.jangmo.web.config.converter.StringToLocalDateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Instant;
import java.time.LocalDate;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry){
		registry.addMapping("/**")
			.allowedOriginPatterns(CorsConfiguration.ALL)
			.allowedHeaders(CorsConfiguration.ALL)
			.allowedMethods(CorsConfiguration.ALL)
			.allowCredentials(true)
			.maxAge(3600L);
	}

	@Override
	public void addFormatters(FormatterRegistry registry){
		registry.addConverter(String.class, Instant.class, new StringToInstantConverter());
		registry.addConverter(String.class, LocalDate.class, new StringToLocalDateConverter());
	}
}
