package com.jangmo.web.repository.user;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.base.BaseRepositoryTest;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Slf4j
public class MemberRepositoryTest extends BaseRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	DistrictRepository districtRepository;

	MemberEntity member;
	City city;
	District district;

	@BeforeEach
	void setup() {
		city = City.of("서울특별시");
		district = District.of("강남구", city);

		cityRepository.save(city);
		districtRepository.save(district);
	}

	@Test
	void 회원_저장_테스트() {
		member = MemberEntity.create(
			"테스트회원",
			"01012341234",
			Gender.MALE,
			LocalDate.of(2002, 2, 14),
			"testPassword",
			city,
			district
		);
		memberRepository.save(member);
		assertNotNull(member.getId());
		log.info("member id :: {}", member.getId());
	}
}
