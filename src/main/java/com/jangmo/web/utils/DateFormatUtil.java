package com.jangmo.web.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 EEEE", Locale.KOREAN);

    public static String convertToString(LocalDate date) {
        return date.format(formatter);
    }

}
