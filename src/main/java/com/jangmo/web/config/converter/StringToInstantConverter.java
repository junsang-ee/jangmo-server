package com.jangmo.web.config.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class StringToInstantConverter implements Converter<String, Instant>  {
	private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
		.parseCaseInsensitive()
		.append(DateTimeFormatter.ISO_LOCAL_DATE)
		.appendLiteral('T')
		.appendValue(ChronoField.HOUR_OF_DAY, 2)
		.appendLiteral(':')
		.appendValue(ChronoField.MINUTE_OF_HOUR, 2)
		.appendLiteral(':')
		.appendValue(ChronoField.SECOND_OF_MINUTE, 2)
		.appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true)
		.appendOffsetId()
		.toFormatter();

    @Override
    public Instant convert(String source){
			try{
				return FORMATTER.parse(source, Instant::from);
			}catch(DateTimeParseException e){
				return DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(source, Instant::from);
			}
    }
}
