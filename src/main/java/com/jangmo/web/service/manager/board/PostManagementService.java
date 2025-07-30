package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.PostCreateRequest;
import com.jangmo.web.model.dto.response.board.PostCreateResponse;

public interface PostManagementService {
    PostCreateResponse create(String memberId, String boardId, PostCreateRequest request);
}
