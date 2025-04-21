package com.jangmo.web.service;

import com.jangmo.web.model.dto.response.SearchPlaceResponse;
import com.jangmo.web.model.dto.response.api.KakaoPlaceSearchResponse;

import java.util.List;

public interface GroundService {
    List<SearchPlaceResponse> searchGrounds(String keyword);
}
