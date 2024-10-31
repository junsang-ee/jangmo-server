package com.jangmo.web.repository;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.entity.administrative.City;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/test_sql/area_city_test.sql")
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;


    @DisplayName("city 데이터 확인 테스트")
    @Test
    void getCityInfoTest() {
        String cityName = "서울특별시";
        Optional<City> seoul = cityRepository.findById(1L);
        assertThat(seoul).isPresent();
        assertThat(seoul.get().getName()).isEqualTo(cityName);
    }

    @DisplayName("city 데이터 not found 테스트")
    @Test
    void getCityNotFoundException() {
        String cityName = "없는 도시명 테스트";
        City city = cityRepository.findByName(cityName).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
    }

    @DisplayName("city 카운팅 테스트")
    @Test
    void getCityCountTest() {
        int count = 17;
        int savedCount = cityRepository.findAll().size();
        assertThat(savedCount).isEqualTo(count);
    }

}
