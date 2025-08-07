package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.manager.PostCreateRequest;
import com.jangmo.web.model.dto.request.board.manager.PostUpdateRequest;
import com.jangmo.web.model.dto.response.board.manager.PostCreateResponse;

public interface PostManagementService {
    PostCreateResponse create(String memberId, String boardId, PostCreateRequest request);

    void update(String postId, PostUpdateRequest request);


}
