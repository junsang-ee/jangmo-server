package com.jangmo.web.model.dto.response;

import com.jangmo.web.model.entity.administrative.District;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class DistrictListResponse {

	private final Long districtId;

	private final String name;

	public static DistrictListResponse of(final District district) {
		return new DistrictListResponse(
			district.getId(),
			district.getName()
		);
	}
}
