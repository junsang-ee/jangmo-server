package com.jangmo.web.model.dto.response;

import com.jangmo.web.constants.GroundType;
import com.jangmo.web.model.entity.GroundEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GroundCreateResponse {
    private final String placeId;
    private final String placeName;
    private final String addressName;
    private final String roadAddressName;
    private final Double longitude;
    private final Double latitude;
    private final String groundType;
    private final String cityName;
    private final String districtName;

    public static GroundCreateResponse of(final GroundEntity ground) {
        return new GroundCreateResponse(
                ground.getPlaceId(),
                ground.getPlaceName(),
                ground.getAddressName(),
                ground.getRoadAddressName(),
                ground.getLongitude(),
                ground.getLatitude(),
                ground.getType() == GroundType.FOOTBALL ? "축구장" : "풋살장",
                ground.getCity().getName(),
                ground.getDistrict().getName()
        );
    }
}
