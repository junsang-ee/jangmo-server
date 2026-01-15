package com.jangmo.web.domain.user;


import com.jangmo.web.constants.Gender;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ActiveProfiles("test")
public class MemberDomainTest {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Test
	void 회원_도메인_생성_성공() {
		String encodedPassword = passwordEncoder.encode("1231231!");
		City city = City.of("서울시");
		District district = District.of("강남구", city);
		MemberEntity member = MemberEntity.create(
			"테스트회원",
			"01012341234",
			Gender.MALE,
			LocalDate.of(1999,9, 9),
			encodedPassword,
			city,
			district
		);
		log.info("member.getStatus :: {}", member.getStatus());
		assertNotNull(member);
	}
}
