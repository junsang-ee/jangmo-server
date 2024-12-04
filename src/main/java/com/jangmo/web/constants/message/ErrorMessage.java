package com.jangmo.web.constants.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    REQUEST_QUERY_PARAM("100"),
    REQUEST_BODY_FIELD("101"),

    /* 1000 ~ 1100 (signup error) */
    CITY_NOT_FOUND("1010"),
    DISTRICT_NOT_FOUND("1011"),

    AUTH_CODE_INVALID("4010"),
    AUTH_CODE_EXPIRED("4011"),

    UNKNOWN("9999");

    @Accessors(fluent = true)
    private final String code;

    public String resName() {
        return "response.error." + name().toLowerCase().replaceAll("_", ".") + ".message";
    }

    public int getCode() {
        return Integer.parseInt(code);
    }

    public static ErrorMessage from(String code) {
        for (ErrorMessage value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException(code);
    }

}
