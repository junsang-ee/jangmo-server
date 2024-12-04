package com.jangmo.web.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String source){
        if(StringUtils.hasText(source)){
            return LocalDate.parse(source);
        }else{
            return null;
        }
    }
}
