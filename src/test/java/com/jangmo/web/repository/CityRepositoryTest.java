package com.jangmo.web.repository;

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

    @DisplayName("city 카운팅 테스트")
    @Test
    void getCityCountTest() {
        int count = 17;
        int savedCount = cityRepository.findAll().size();
        assertThat(savedCount).isEqualTo(count);
    }

}
