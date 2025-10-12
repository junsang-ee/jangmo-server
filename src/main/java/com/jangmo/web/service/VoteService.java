package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.vote.MatchVoteCastRequest;
import com.jangmo.web.model.dto.response.vote.UserMatchVoteStatusResponse;

public interface VoteService {

    void castMatchVote(String matchVoteId, String userId, MatchVoteCastRequest request);

    UserMatchVoteStatusResponse getMatchVoteStatus(String matchVoteId, String userId);
}
