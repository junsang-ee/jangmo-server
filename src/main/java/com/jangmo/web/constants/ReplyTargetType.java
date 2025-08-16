package com.jangmo.web.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReplyTargetType {
    POST, MATCH_VOTE, GENERAL_VOTE;

    @JsonCreator
    public static ReplyTargetType getValue(String value) {
        for(ReplyTargetType target : values()) {
            if(target.name().equals(value)) {
                return target;
            }
        }
        return null;
    }
}
