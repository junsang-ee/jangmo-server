package com.jangmo.web.controller;

import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;


@RequiredArgsConstructor
@RequestMapping("/api/locations")
@RestController
public class LocationController {

	private final LocationService locationService;


	@GetMapping("/cities")
	public ResponseEntity<ApiSuccessResponse<List<CityListResponse>>> cities() {
		return wrap(locationService.getCityList());
	}

	@GetMapping("/cities/{cityId}/districts")
	public ResponseEntity<ApiSuccessResponse<List<DistrictListResponse>>> districts(@PathVariable Long cityId) {
		return wrap(locationService.getDistrictsByCityId(cityId));
	}

	@GetMapping("/cities/name/{cityName}/districts")
	public ResponseEntity<ApiSuccessResponse<List<DistrictListResponse>>> districts(@PathVariable String cityName) {
		return wrap(locationService.getDistrictsByCityName(cityName));
	}
}
