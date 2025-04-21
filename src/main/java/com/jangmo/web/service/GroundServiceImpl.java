package com.jangmo.web.service;

import com.jangmo.web.api.WebClientApiHelper;
import com.jangmo.web.constants.ApiType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.dto.response.SearchPlaceResponse;
import com.jangmo.web.model.dto.response.api.KakaoPlaceSearchResponse;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.utils.LocationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroundServiceImpl implements GroundService {

    private final WebClientApiHelper webClientApiHelper;

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;

    @Qualifier("kakaoWebClient")
    private final WebClient webClient;

    public List<SearchPlaceResponse> searchGrounds(String keyword) {
        KakaoPlaceSearchResponse apiResponse = webClientApiHelper.get(
                webClient, ApiType.KAKAO, keyword, KakaoPlaceSearchResponse.class
        ).block();
        if (apiResponse == null) return null;
        return apiResponse.getDocuments().stream().map(
            document -> {
                String[] addressParts = document.getAddressName().split("\\s+");
                String cityName = addressParts.length > 0 ?
                        LocationUtil.getStandardCityName(addressParts[0]) : "";
                String districtName = addressParts.length > 1 ? addressParts[1] : "";
                City city = cityRepository.findByName(LocationUtil.getStandardCityName(cityName))
                        .orElseThrow(
                                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
                        );
                District district = districtRepository.findByCityAndName(city, districtName)
                        .orElseThrow(
                                () -> new NotFoundException(ErrorMessage.DISTRICT_NOT_FOUND)
                        );
                return SearchPlaceResponse.of(document, city.getId(), district.getId());
            }
        ).collect(Collectors.toList());
    }


}
