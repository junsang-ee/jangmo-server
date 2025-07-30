package com.jangmo.web.model.dto.response.board.manager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCreateResponse {

    private final String title;

    public static PostCreateResponse of(final String title) {
        return new PostCreateResponse(title);
    }

}
