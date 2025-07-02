package com.jangmo.web.model.dto.response.vote;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchVoteCreateResponse {

    private final LocalDate startAt;

    private final LocalDate endAt;

    private final LocalDate matchAt;

    private final String creatorName;

    public static MatchVoteCreateResponse of(final LocalDate startAt,
                                             final LocalDate endAt,
                                             final LocalDate matchAt,
                                             final String creatorName) {
        return new MatchVoteCreateResponse(
                startAt, endAt, matchAt, creatorName
        );
    }

}
