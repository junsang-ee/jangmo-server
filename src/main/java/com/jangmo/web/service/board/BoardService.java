package com.jangmo.web.service.board;

import com.jangmo.web.model.dto.response.board.BoardListResponse;

import java.util.List;

public interface BoardService {

    List<BoardListResponse> list();
}
