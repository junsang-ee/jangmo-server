package com.jangmo.web.repository;

import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.repository.base.BaseRepositoryTest;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class CityRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private CityRepository cityRepository;

	static final String CITY_NAME = "서울특별시";

	@Test
	void 시도_저장_테스트() {
		City city = City.of(CITY_NAME);
		cityRepository.save(city);
		assertNotNull(city.getId());
		log.info("city id : {}", city.getId());
	}

}
