package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.entity.MatchVoteEntity;
import com.jangmo.web.model.entity.user.UserEntity;

public interface VoteService {
    MatchVoteEntity createMatchVote(UserEntity writer, MatchVoteCreateRequest request);
}
