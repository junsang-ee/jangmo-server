package com.jangmo.web.utils;

import java.time.LocalDate;
import java.time.Period;

public class AgeUtil {
    public static int calculate(LocalDate birth) {
        LocalDate now = LocalDate.now();
        int age = Period.between(birth, now).getYears();

        if (birth.plusYears(age).isAfter(now)) {
            age--;
        }
        return age;
    }
}
