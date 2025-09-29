package com.jangmo.web.service.manager;

import com.jangmo.web.constants.ApiType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.infra.client.WebClientExecutor;
import com.jangmo.web.model.dto.request.GroundCreateRequest;
import com.jangmo.web.model.dto.response.GroundCreateResponse;
import com.jangmo.web.model.dto.response.SearchPlaceResponse;
import com.jangmo.web.model.dto.response.api.KakaoPlaceSearchResponse;
import com.jangmo.web.model.entity.GroundEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.user.MemberRepository;
import com.jangmo.web.repository.user.UserRepository;
import com.jangmo.web.utils.LocationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroundManagementServiceImpl implements GroundManagementService {
    private final WebClientExecutor webClientExecutor;

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;

    private final UserRepository userRepository;

    private final MemberRepository memberRepository;

    @Qualifier("kakaoWebClient")
    private final WebClient webClient;

    @Override
    public List<SearchPlaceResponse> searchGrounds(String searcherId, String keyword) {
        KakaoPlaceSearchResponse apiResponse = webClientExecutor.get(
                webClient, ApiType.KAKAO, keyword, KakaoPlaceSearchResponse.class
        ).block();
        if (apiResponse == null) return null;
        return apiResponse.getDocuments().stream()
                .filter(
                        document -> getIsGroundPlace(document.getCategoryName())
                ).map(document -> {
                    String[] addressParts = document.getAddressName().split("\\s+");
                    String cityName = addressParts.length > 0 ?
                            LocationUtil.getStandardCityName(addressParts[0]) : "";
                    String districtName = addressParts.length > 1 ? addressParts[1] : "";
                    City city = getCityByName(LocationUtil.getStandardCityName(cityName));
                    District district = getDistrictByCityAndName(city, districtName);
                    return SearchPlaceResponse.of(document, city.getId(), district.getId());
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GroundCreateResponse createGround(String createdById, GroundCreateRequest request) {
        City city = getCityById(request.getCityId());
        District district = getDistrictById(request.getDistrictId());
        MemberEntity createdBy = getMemberById(createdById);
        GroundEntity ground = GroundEntity.create(
                createdBy,
                request.getPlaceId(),
                request.getPlaceName(),
                request.getAddressName(),
                request.getRoadAddressName(),
                request.getLongitude(),
                request.getLatitude(),
                request.getGroundType(),
                city, district
        );
        return GroundCreateResponse.of(ground);
    }

    private boolean getIsGroundPlace(String category) {
        String[] categories = category.split(">");
        String mainCategory = categories[0].trim();
        String subCategory = categories[1].trim();
        return mainCategory.contains("스포츠,레저") && subCategory.contains("축구");
    }

    private MemberEntity getMemberById(String memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
    }

    private City getCityById(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
    }

    private District getDistrictById(Long districtId) {
        return districtRepository.findById(districtId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.DISTRICT_NOT_FOUND)
        );
    }

    private City getCityByName(String cityName) {
        return cityRepository.findByName(LocationUtil.getStandardCityName(cityName))
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
                );
    }

    private District getDistrictByCityAndName(City city, String districtName) {
        return districtRepository.findByCityAndName(city, districtName)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.DISTRICT_NOT_FOUND)
                );
    }
}
