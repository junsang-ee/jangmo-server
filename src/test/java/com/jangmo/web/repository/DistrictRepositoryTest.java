package com.jangmo.web.repository;

import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/test_sql/area_city_test.sql")
@Sql("/test_sql/area_district_test.sql")
@DataJpaTest
public class DistrictRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @DisplayName("district 데이터 확인 테스트")
    @Test
    void getDistrictInfoTest() {
        City seoul = cityRepository.findById(1L).get();
        String districtName = "강남구";
        List<District> gangNam = districtRepository.findByCity(seoul);
        assertThat(gangNam.get(22).getCity()).isEqualTo(seoul);
        assertThat(gangNam.get(22).getName()).isEqualTo(districtName);
    }

    @DisplayName("district 카운팅 테스트")
    @Test
    void getDistrictCountTest() {
        int count = 229;
        int savedCount = districtRepository.findAll().size();
        assertThat(savedCount).isEqualTo(count);
    }

}
