package com.jangmo.web.repository;

import com.jangmo.web.config.JpaConfiguration;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfiguration.class)
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    static final String CITY_NAME = "서울특별시";
    static final String DISTRICT_NAME = "강남구";

    @Test
    void 시도_저장_테스트() {
        City city = City.of(CITY_NAME);
        cityRepository.save(city);
        assertNotNull(city.getId());
        log.info("city id : {}", city.getId());
    }


}
