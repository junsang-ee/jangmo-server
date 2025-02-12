package com.jangmo.web.service.manager;

import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.MatchVoteCreateResponse;

public interface VoteService {
    MatchVoteCreateResponse createMatchVote(String userId, MatchVoteCreateRequest request);
}
