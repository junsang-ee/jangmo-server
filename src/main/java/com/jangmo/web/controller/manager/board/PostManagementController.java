package com.jangmo.web.controller.manager.board;

import com.jangmo.web.model.dto.request.board.manager.PostUpdateRequest;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.manager.board.PostManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/managers/posts")
@RestController
public class PostManagementController {

    private final PostManagementService postManagementService;

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiSuccessResponse<Object>> update(
            @PathVariable String postId,
            @Valid @RequestBody PostUpdateRequest request) {
        postManagementService.update(postId, request);
        return wrap(null);
    }



}
