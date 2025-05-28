package com.jangmo.web.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AuthPurposeType {
    SIGNUP, RESET_PASSWORD, RESET_MERCENARY_CODE;

    @JsonCreator
    public static AuthPurposeType getValue(String value) {
        for (AuthPurposeType purposeType : values()) {
            if (purposeType.name().equals(value))
                return purposeType;
        }
        return null;
    }
}
