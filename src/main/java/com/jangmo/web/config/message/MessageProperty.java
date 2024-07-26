package com.jangmo.web.config.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.messages")
@Configuration
public class MessageProperty {
    private String basename;
    private String encoding;
    private int cacheDuration;
    private boolean alwaysUseMessageFormat;
    private boolean useCodeAsDefaultMessage;
    private boolean fallbackToSystemLocale;

    @Bean
    public MessageSource messageSource() {
        YamlMessageSource source = new YamlMessageSource();
        source.setBasename(this.basename);
        source.setDefaultEncoding(this.encoding);
        source.setCacheSeconds(this.cacheDuration);
        source.setAlwaysUseMessageFormat(this.alwaysUseMessageFormat);
        source.setUseCodeAsDefaultMessage(this.useCodeAsDefaultMessage);
        source.setFallbackToSystemLocale(this.fallbackToSystemLocale);
        return source;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(){
        return new MessageSourceAccessor(messageSource());
    }
}
