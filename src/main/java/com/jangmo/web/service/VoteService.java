package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.vote.MatchVoteCastRequest;

public interface VoteService {

    void castMatchVote(String matchVoteId, String userId, MatchVoteCastRequest request);
}
