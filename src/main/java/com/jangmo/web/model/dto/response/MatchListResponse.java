package com.jangmo.web.model.dto.response;

import com.jangmo.web.model.entity.MatchEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MatchListResponse {

	private final String matchId;

	private final LocalDate matchAt;

	public static MatchListResponse of(final MatchEntity match) {
		return new MatchListResponse(
			match.getId(),
			match.getMatchAt()
		);
	}
}
