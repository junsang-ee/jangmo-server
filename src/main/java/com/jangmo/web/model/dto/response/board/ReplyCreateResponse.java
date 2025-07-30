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

    private final ReplyTargetType parentTarget;

    public static ReplyCreateResponse of(final ReplyEntity reply) {
        return new ReplyCreateResponse(
                reply.getContent(),
                reply.getTargetType()
        );
    }

}
