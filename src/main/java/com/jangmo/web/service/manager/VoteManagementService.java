package com.jangmo.web.service.manager;

import com.jangmo.web.model.dto.request.vote.GeneralVoteCreateRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.vote.GeneralVoteCreateResponse;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;

public interface VoteManagementService {
    MatchVoteCreateResponse createMatchVote(String userId, MatchVoteCreateRequest request);

    GeneralVoteCreateResponse createGeneralVote(String userId, GeneralVoteCreateRequest request);
}
