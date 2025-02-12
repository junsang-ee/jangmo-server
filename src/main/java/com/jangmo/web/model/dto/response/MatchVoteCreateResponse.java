package com.jangmo.web.model.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchVoteCreateResponse {

    private final LocalDate matchAt;

    private final String creatorName;

    public static MatchVoteCreateResponse of(final LocalDate matchAt,
                                             final String creatorName) {
        return new MatchVoteCreateResponse(
                matchAt, creatorName
        );
    }

}
