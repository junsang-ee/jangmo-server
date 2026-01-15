package com.jangmo.web.controller.board;

import com.jangmo.web.constants.ReplyTargetType;
import com.jangmo.web.model.dto.request.board.ReplyCreateRequest;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.entity.board.PostEntity;
import com.jangmo.web.service.ReplyService;
import com.jangmo.web.service.board.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

	private final PostService postService;

	private final ReplyService replyService;

	@GetMapping("/{postId}")
	public ResponseEntity<ApiSuccessResponse<PostEntity>> detail(
		@PathVariable String postId
	) {
		return wrap(postService.detail(postId));
	}

	@PostMapping("/{postId}/replies")
	public ResponseEntity<ApiSuccessResponse<Object>> createReply(
		@AuthenticationPrincipal(expression = "id") String memberId,
		@PathVariable String postId,
		@Valid @RequestBody ReplyCreateRequest request
	) {
		replyService.create(memberId, postId, ReplyTargetType.POST, request);
		return wrap(null);
	}

}
