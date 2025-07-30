package com.jangmo.web.controller.manager;

import com.jangmo.web.model.dto.request.board.BoardCreateRequest;
import com.jangmo.web.model.dto.response.board.BoardCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.manager.board.BoardManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/managers/boards")
@RestController
public class BoardManagementController {

    private final BoardManagementService boardManagementService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<BoardCreateResponse>> create(
            @Valid @RequestBody BoardCreateRequest request) {
        return wrap(boardManagementService.create(request));
    }

}
