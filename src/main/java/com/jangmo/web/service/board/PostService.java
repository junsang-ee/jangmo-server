package com.jangmo.web.service.board;

import com.jangmo.web.model.dto.response.board.PostListResponse;
import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.model.entity.board.PostEntity;

import java.util.List;

public interface PostService {

    List<PostListResponse> list(String boardId);

    PostEntity detail(String postId);
}
