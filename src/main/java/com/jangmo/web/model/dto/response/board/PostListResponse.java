package com.jangmo.web.model.dto.response.board;

import com.jangmo.web.model.entity.board.PostEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostListResponse {

	private final String id;
	private final String title;
	private final String writerName;
	private final Instant createdAt;

	public static PostListResponse of(final PostEntity post) {
		return new PostListResponse(
			post.getId(),
			post.getTitle(),
			post.getCreatedBy().getName(),
			post.getCreatedAt()
		);
	}
}
