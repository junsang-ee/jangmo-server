package com.jangmo.web.model.dto.response.board;

import com.jangmo.web.model.entity.board.BoardEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardListResponse {

    private final String boardId;

    private final String name;

    public static BoardListResponse of(final BoardEntity board) {
        return new BoardListResponse(
                board.getId(),
                board.getName()
        );
    }

}
