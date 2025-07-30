package com.jangmo.web.controller;

import com.jangmo.web.model.dto.request.board.ReplyCreateRequest;
import com.jangmo.web.model.dto.response.board.ReplyCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<ReplyCreateResponse>> create(
            @AuthenticationPrincipal(expression = "id") String memberId,
            @Valid @RequestBody ReplyCreateRequest request) {
        return wrap(replyService.create(memberId, request));
    }

}
