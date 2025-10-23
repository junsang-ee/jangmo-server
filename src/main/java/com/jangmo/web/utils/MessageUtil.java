package com.jangmo.web.utils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

    @Resource
    private MessageSource source;

    static MessageSource messageSource;

    @PostConstruct
    public void initialize() {
        messageSource = source;
    }

    public static String getMessage(String message) {
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String message, @Nullable String[] args) {
        return messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
    }
}
