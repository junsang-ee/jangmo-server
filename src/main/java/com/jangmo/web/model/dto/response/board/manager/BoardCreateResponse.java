package com.jangmo.web.model.dto.response.board.manager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateResponse {

	private final String name;

	public static BoardCreateResponse of(final String name) {
		return new BoardCreateResponse(name);
	}
}
