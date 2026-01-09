package com.jangmo.web.controller;

import com.jangmo.web.model.dto.response.MatchListResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/matches")
@RestController
public class MatchController {

	private final MatchService matchService;

	@GetMapping("/upcoming")
	public ResponseEntity<ApiSuccessResponse<List<MatchListResponse>>> upcomingMatches() {
		return wrap(matchService.getUpcomingMatchList());
	}
}
