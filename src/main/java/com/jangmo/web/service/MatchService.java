package com.jangmo.web.service;

import com.jangmo.web.model.dto.response.MatchListResponse;

import java.util.List;

public interface MatchService {

	List<MatchListResponse> getUpcomingMatchList();
}
