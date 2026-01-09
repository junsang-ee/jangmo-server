package com.jangmo.web.service;

import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;

import java.util.List;

public interface LocationService {

	List<CityListResponse> getCityList();

	List<DistrictListResponse> getDistrictsByCityId(Long cityId);

	List<DistrictListResponse> getDistrictsByCityName(String cityName);
}
