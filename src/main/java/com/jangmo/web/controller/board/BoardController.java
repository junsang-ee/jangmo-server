package com.jangmo.web.controller.board;

import com.jangmo.web.model.dto.response.board.BoardListResponse;
import com.jangmo.web.model.dto.response.board.PostListResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.board.BoardService;
import com.jangmo.web.service.board.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardController {

	private final BoardService boardService;

	private final PostService postService;

	@GetMapping
	public ResponseEntity<ApiSuccessResponse<List<BoardListResponse>>> list() {
		return wrap(boardService.list());
	}

	@GetMapping("/{boardId}/posts")
	public ResponseEntity<ApiSuccessResponse<List<PostListResponse>>> postList(@PathVariable String boardId) {
		return wrap(postService.list(boardId));
	}
}
