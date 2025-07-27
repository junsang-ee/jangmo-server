package com.jangmo.web.service.manager;

import com.jangmo.web.model.dto.request.GroundCreateRequest;
import com.jangmo.web.model.dto.response.GroundCreateResponse;
import com.jangmo.web.model.dto.response.SearchPlaceResponse;

import java.util.List;

public interface GroundManagementService {
    List<SearchPlaceResponse> searchGrounds(String searcherId, String keyword);

    GroundCreateResponse createGround(String createdById, GroundCreateRequest request);
}
