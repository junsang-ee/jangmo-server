package com.jangmo.web.model.dto.response.vote;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralVoteCreateResponse {

    private final LocalDate startAt;

    private final LocalDate endAt;

    private final String creatorName;

    public static GeneralVoteCreateResponse of(final LocalDate startAt,
                                               final LocalDate endAt,
                                               final String creatorName) {
        return new GeneralVoteCreateResponse(
                startAt, endAt, creatorName
        );
    }

}
