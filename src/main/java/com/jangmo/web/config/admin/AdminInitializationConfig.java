package com.jangmo.web.config.admin;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminInitializationConfig implements ApplicationRunner {
    private final MemberRepository memberRepository;

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;

    @Value("${jangmo.admin.mobile}")
    private String mobile;

    @Value("${jangmo.admin.password}")
    private String password;

    @Value("${jangmo.admin.name}")
    private String name;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("Administrator Account initialize...");
        if (memberRepository.findByMobile("01043053451").isPresent()) {
            log.info("Administrator's Account is already exists!");
            log.info("Exit Administrator initialize");
            return;
        }
        City city = cityRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
        District district = districtRepository.findById(21L).orElseThrow(
                () -> new NotFoundException(ErrorMessage.DISTRICT_NOT_FOUND)
        );
        MemberSignUpRequest signup = new MemberSignUpRequest(
                name,
                mobile,
                Gender.MALE,
                LocalDate.of(1994, 3, 16),
                password,
                city.getId(),
                district.getId()
        );

        MemberEntity admin = MemberEntity.create(
                signup, city, district
        );
        admin.updateStatus(MemberStatus.ENABLED);
        admin.updateRole(UserRole.ADMIN);
        memberRepository.save(admin);
        log.info("Complete Create Administrator Account!");
        log.info("Exit initialize");
    }
}
