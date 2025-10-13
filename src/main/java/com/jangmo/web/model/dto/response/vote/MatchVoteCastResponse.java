package com.jangmo.web.model.dto.response.vote;

import com.jangmo.web.constants.vote.MatchVoteOption;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchVoteCastResponse {

    private final MatchVoteOption selectedOption;

    public static MatchVoteCastResponse of(final MatchVoteOption option) {
        return new MatchVoteCastResponse(option);
    }
}
