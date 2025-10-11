package com.jangmo.web.constants.vote;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MatchVoteOption {
    NOT_VOTED, ANTE_MERIDIEM, POST_MERIDIEM, ABSENT, PENDING;

    @JsonCreator
    public static MatchVoteOption getValue(String value) {
        for (MatchVoteOption option : values()) {
            if (option.name().equals(value)) {
                return option;
            }
        }
        return null;
    }
}
