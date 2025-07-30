package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.BoardCreateRequest;
import com.jangmo.web.model.dto.response.board.BoardCreateResponse;

public interface BoardManagementService {

    BoardCreateResponse create(BoardCreateRequest request);
}
