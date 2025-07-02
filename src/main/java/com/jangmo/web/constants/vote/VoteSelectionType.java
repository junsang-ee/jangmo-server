package com.jangmo.web.constants.vote;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VoteSelectionType {
    SINGLE, MULTIPLE;

    @JsonCreator
    public static VoteSelectionType getValue(String value) {
        for(VoteSelectionType type : values()) {
            if(type.name().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
