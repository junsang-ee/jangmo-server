package com.jangmo.web.controller.board;

import com.jangmo.web.model.dto.request.board.manager.PostUpdateRequest;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.entity.board.PostEntity;
import com.jangmo.web.service.board.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<ApiSuccessResponse<PostEntity>> detail(
            @PathVariable String postId) {
        return wrap(postService.detail(postId));
    }

}
