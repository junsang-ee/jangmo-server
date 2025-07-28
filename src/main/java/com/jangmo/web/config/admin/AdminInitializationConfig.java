package com.jangmo.web.config.admin;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
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
    private String adminMobile;

    @Value("${jangmo.admin.password}")
    private String adminPassword;

    @Value("${jangmo.admin.name}")
    private String adminName;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("Administrator Account initialize...");
        memberRepository.findByMobile(adminMobile).ifPresentOrElse(
                admin -> {
                    log.info("Administrator's Account is already exists!");
                    initializeAdmin(admin);
                }, () -> {
                    log.info("Administrator's Account is Not! exists");
                    log.info("Create Administrator`s Account...");
                    City city = cityRepository.findById(1L).orElseThrow(
                            () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
                    );
                    District district = districtRepository.findById(21L).orElseThrow(
                            () -> new NotFoundException(ErrorMessage.DISTRICT_NOT_FOUND)
                    );
                    MemberSignUpRequest signup = new MemberSignUpRequest(
                            adminName,
                            adminMobile,
                            Gender.MALE,
                            LocalDate.of(1994, 3, 16),
                            adminPassword,
                            city.getId(),
                            district.getId()
                    );
                    MemberEntity admin = MemberEntity.create(
                            signup, city, district
                    );
                    memberRepository.save(admin);
                    initializeAdmin(admin);
                }
        );
        log.info("Complete Create Administrator Account!");
        log.info("Exit Administrator initialize");
    }

    private void initializeAdmin(MemberEntity admin) {
        admin.updateRole(UserRole.ADMIN);
        admin.updateStatus(MemberStatus.ENABLED);
        if (admin.getKakaoApiUsage() == null)
            admin.createKakaoApiUsage();
    }
}
