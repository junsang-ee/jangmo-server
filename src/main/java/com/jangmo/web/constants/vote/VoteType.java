package com.jangmo.web.constants.vote;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VoteType {
    MATCH, GENERAL;

    @JsonCreator
    public static VoteType getValue(String value) {
        for(VoteType type : values()) {
            if(type.name().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
