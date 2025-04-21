package com.jangmo.web.model.dto.response;

import com.jangmo.web.model.dto.response.api.KakaoPlaceSearchResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class SearchPlaceResponse {
    private final String placeId;
    private final String placeName;
    private final String roadAddressName;
    private final String addressName;
    private final Double longitude;
    private final Double latitude;
    private final Long cityId;
    private final Long districtId;

    public static SearchPlaceResponse of(final KakaoPlaceSearchResponse.Document document,
                                         final Long cityId,
                                         final Long districtId) {
        return new SearchPlaceResponse(
                document.getPlaceId(),
                document.getPlaceName(),
                document.getRoadAddressName(),
                document.getAddressName(),
                document.getLongitude(),
                document.getLatitude(),
                cityId,
                districtId
        );
    }

}
