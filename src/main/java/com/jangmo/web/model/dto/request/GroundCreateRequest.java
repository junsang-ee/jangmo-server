package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.GroundType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroundCreateRequest {
	private String placeId;
	private String placeName;
	private String addressName;
	private String roadAddressName;
	private Double longitude;
	private Double latitude;
	private GroundType groundType;
	private Long cityId;
	private Long districtId;
}
