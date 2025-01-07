package com.jangmo.web.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE, FEMALE;

    @JsonCreator
    public static Gender getValue(String value) {
        for(Gender gender : values()) {
            if(gender.name().equals(value)) {
                return gender;
            }
        }
        return null;
    }


}
