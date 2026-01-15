package com.jangmo.web.controller.manager.board;

import com.jangmo.web.model.dto.request.board.manager.BoardCreateRequest;
import com.jangmo.web.model.dto.request.board.manager.BoardUpdateRequest;
import com.jangmo.web.model.dto.request.board.manager.PostCreateRequest;
import com.jangmo.web.model.dto.response.board.manager.BoardCreateResponse;
import com.jangmo.web.model.dto.response.board.manager.PostCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.manager.board.BoardManagementService;
import com.jangmo.web.service.manager.board.PostManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/managers/boards")
@RestController
public class BoardManagementController {

	private final BoardManagementService boardManagementService;

	private final PostManagementService postManagementService;

	@PostMapping
	public ResponseEntity<ApiSuccessResponse<BoardCreateResponse>> create(
		@Valid @RequestBody BoardCreateRequest request
	) {
		return wrap(boardManagementService.create(request));
	}

	@PatchMapping("/{boardId}")
	public ResponseEntity<ApiSuccessResponse<Object>> update(
		@PathVariable String boardId,
		@RequestBody BoardUpdateRequest request
	) {
		boardManagementService.update(boardId, request);
		return wrap(null);
	}

	@PostMapping("/{boardId}/posts")
	public ResponseEntity<ApiSuccessResponse<PostCreateResponse>> createPost(
		@AuthenticationPrincipal(expression = "id") String memberId,
		@PathVariable String boardId,
		@Valid @RequestBody PostCreateRequest request
	) {
		return wrap(postManagementService.create(memberId, boardId, request));
	}

}
