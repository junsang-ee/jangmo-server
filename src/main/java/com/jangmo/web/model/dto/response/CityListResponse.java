package com.jangmo.web.model.dto.response;

import com.jangmo.web.model.entity.administrative.City;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CityListResponse {
    private final Long id;
    private final String name;

    public static CityListResponse of(final City city) {
        return new CityListResponse(
                city.getId(),
                city.getName()
        );
    }
}
