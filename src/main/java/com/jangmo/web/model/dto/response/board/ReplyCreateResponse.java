package com.jangmo.web.model.dto.response.board;

import com.jangmo.web.constants.ReplyTargetType;
import com.jangmo.web.model.entity.board.ReplyEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyCreateResponse {

    private final String content;

    public static ReplyCreateResponse of(final String content) {
        return new ReplyCreateResponse(content);
    }

}