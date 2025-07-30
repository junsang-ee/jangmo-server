package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.board.ReplyCreateRequest;
import com.jangmo.web.model.dto.response.board.ReplyCreateResponse;

public interface ReplyService {

    ReplyCreateResponse create(String memberId, ReplyCreateRequest request);
}
