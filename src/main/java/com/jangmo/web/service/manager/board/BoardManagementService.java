package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.manager.BoardCreateRequest;
import com.jangmo.web.model.dto.response.board.manager.BoardCreateResponse;

public interface BoardManagementService {

    BoardCreateResponse create(BoardCreateRequest request);
}
