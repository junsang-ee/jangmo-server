package com.jangmo.web.controller.manager;


import com.jangmo.web.model.dto.request.board.manager.PostCreateRequest;
import com.jangmo.web.model.dto.response.board.manager.PostCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.manager.board.PostManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/managers/boards")
@RestController
public class PostManagementController {

    private final PostManagementService postManagementService;

    @PostMapping("/{boardId}/posts")
    public ResponseEntity<ApiSuccessResponse<PostCreateResponse>> create(
            @AuthenticationPrincipal(expression = "id") String memberId,
            @PathVariable String boardId,
            @Valid @RequestBody PostCreateRequest request) {
        return wrap(postManagementService.create(memberId, boardId, request));
    }
}
