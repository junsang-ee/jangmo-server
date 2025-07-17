package com.jangmo.web.constants.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    REQUEST_QUERY_PARAM(100, HttpStatus.BAD_REQUEST),
    REQUEST_BODY_FIELD(101, HttpStatus.BAD_REQUEST),
    REQUEST_INVALID_MOBILE(110, HttpStatus.BAD_REQUEST),
    REQUEST_INVALID_PASSWORD(111, HttpStatus.BAD_REQUEST),
    REQUEST_INVALID_NAME(112, HttpStatus.BAD_REQUEST),
    REQUEST_INVALID_CODE(113, HttpStatus.BAD_REQUEST),
    REQUEST_INVALID_MERCENARY_CODE(114, HttpStatus.BAD_REQUEST),

    BAD_REQUEST(400, HttpStatus.BAD_REQUEST),

    /* 1000 ~ 1100 (signup error) */
    CITY_NOT_FOUND(1010, HttpStatus.NOT_FOUND),
    DISTRICT_NOT_FOUND(1011, HttpStatus.NOT_FOUND),

    AUTH_EXPIRED(4000, HttpStatus.UNAUTHORIZED),
    AUTH_PERMISSION_DENIED(4001, HttpStatus.FORBIDDEN),
    AUTH_INVALID(4002, HttpStatus.UNAUTHORIZED),
    AUTH_PASSWORD_INVALID(4003, HttpStatus.UNAUTHORIZED),
    AUTH_DISABLED(4004, HttpStatus.FORBIDDEN),
    AUTH_UNAUTHENTICATED(4005, HttpStatus.FORBIDDEN),
    AUTH_RETIRED(4006, HttpStatus.FORBIDDEN),
    AUTH_ALREADY_ENABLED(4007, HttpStatus.BAD_REQUEST),
    AUTH_MERCENARY_CODE_INVALID(4008, HttpStatus.BAD_REQUEST),
    AUTH_MERCENARY_EXPIRED(4009, HttpStatus.BAD_REQUEST),
    AUTH_MERCENARY_PRIVACY(4010, HttpStatus.BAD_REQUEST),
    AUTH_MERCENARY_CODE_NOT_ISSUED(4011, HttpStatus.UNAUTHORIZED),
    AUTH_USER_DUPLICATED(4012, HttpStatus.CONFLICT),
    AUTH_CODE_INVALID(4020, HttpStatus.BAD_REQUEST),
    AUTH_NOT_VERIFIED(4021, HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND(5000, HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND(5001, HttpStatus.NOT_FOUND),
    MERCENARY_NOT_FOUND(5002, HttpStatus.NOT_FOUND),
    MATCH_NOT_FOUND(5010, HttpStatus.NOT_FOUND),

    MEMBER_ALREADY_ENABLED(6000, HttpStatus.BAD_REQUEST),
    MEMBER_DISABLED(6001, HttpStatus.FORBIDDEN),
    MEMBER_RETIRED(6002, HttpStatus.FORBIDDEN),
    MEMBER_ALREADY_HAS_ROLE(6003, HttpStatus.BAD_REQUEST),
    MERCENARY_DISABLED(6010, HttpStatus.FORBIDDEN),
    MERCENARY_ALREADY_ENABLED(6011, HttpStatus.BAD_REQUEST),
    MERCENARY_ALREADY_TRANSIENT_EXISTS(6012, HttpStatus.CONFLICT),
    MERCENARY_NEEDS_CODE_REISSUE(6013, HttpStatus.BAD_REQUEST),
    MERCENARY_NEEDS_STATUS(6014, HttpStatus.BAD_REQUEST),

    /* System, Server, Spring Error */
    INTERNAL_SERVER_ERROR(9998, HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN(9999, HttpStatus.INTERNAL_SERVER_ERROR);

    @Accessors(fluent = true)
    private final int code;

    @Accessors(fluent = true)
    private final HttpStatus status;

    public String resName() {
        return "response.error." + name().toLowerCase().replaceAll("_", ".") + ".message";
    }

    public int getStatusCode() {
        return status.value();
    }

    public static ErrorMessage from(int code) {
        for (ErrorMessage value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(String.valueOf(code));
    }
}
