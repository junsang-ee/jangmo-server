package com.jangmo.web.controller.manager;

import com.jangmo.web.model.dto.request.GroundCreateRequest;
import com.jangmo.web.model.dto.response.GroundCreateResponse;
import com.jangmo.web.model.dto.response.SearchPlaceResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.manager.GroundManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/managers/grounds")
@RestController
public class GroundManagementController {

    private final GroundManagementService groundManagementService;

    @GetMapping("/{keyword}")
    public ResponseEntity<ApiSuccessResponse<List<SearchPlaceResponse>>> searchGrounds(
            @AuthenticationPrincipal(expression = "id") String searcherId,
            @PathVariable String keyword) {
        return wrap(groundManagementService.searchGrounds(searcherId, keyword));
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<GroundCreateResponse>> createGround(
            @AuthenticationPrincipal(expression = "id") String createdById,
            @RequestBody GroundCreateRequest request) {
        return wrap(groundManagementService.createGround(createdById, request));
    }

}
