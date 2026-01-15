package com.jangmo.web.model.dto.response.vote;

import com.jangmo.web.constants.vote.MatchVoteOption;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMatchVoteStatusResponse {

	private final boolean isVoted;

	private final MatchVoteOption selectedOption;

	public static UserMatchVoteStatusResponse of(
		final boolean isVoted,
		final MatchVoteOption selectedOption
	) {
		return new UserMatchVoteStatusResponse(isVoted, selectedOption);
	}

}
