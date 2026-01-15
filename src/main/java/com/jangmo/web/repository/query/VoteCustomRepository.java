package com.jangmo.web.repository.query;

import com.jangmo.web.model.dto.request.vote.VoteListRequest;
import com.jangmo.web.model.dto.response.vote.VoteListResponse;

import java.util.List;

public interface VoteCustomRepository {

	List<VoteListResponse> findVotes(VoteListRequest request);
}
