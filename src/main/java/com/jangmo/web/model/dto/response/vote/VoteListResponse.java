package com.jangmo.web.model.dto.response.vote;

import com.jangmo.web.constants.vote.VoteType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PUBLIC;

@Getter
@RequiredArgsConstructor(access = PUBLIC)
public class VoteListResponse {

	private final String voteId;
	private final String title;
	private final VoteType voteType;
	private final LocalDate startAt;
	private final LocalDate endAt;

}
