package com.jangmo.web.service;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;

    @Override
    public List<CityListResponse> getCityList() {
        return cityRepository.findAll().stream().map(
                CityListResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public List<DistrictListResponse> getDistrictsByCityId(Long cityId) {
        City city = getCity(cityId);
        return districtRepository.findByCity(city).stream().map(
                DistrictListResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public List<DistrictListResponse> getDistrictsByCityName(String cityName) {
        City city = getCityByCityName(cityName);
        return districtRepository.findByCity(city).stream().map(
                DistrictListResponse::of
        ).collect(Collectors.toList());
    }

    private City getCity(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
    }

    private City getCityByCityName(String cityName) {
        return cityRepository.findByName(cityName).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
    }
}
