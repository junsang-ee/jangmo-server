package com.jangmo.web.service;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MemberStatus;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.MercenaryStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.MatchVoteEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;
import com.jangmo.web.repository.*;
import com.jangmo.web.service.manager.UserManagementServiceImpl;
import com.jangmo.web.service.manager.VoteServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@RequiredArgsConstructor
@SpringBootTest
public class UserManagementServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MercenaryRepository mercenaryRepository;
    @Autowired CityRepository cityRepository;
    @Autowired DistrictRepository districtRepository;
    @Autowired MatchRepository matchRepository;
    @Autowired MatchVoteRepository matchVoteRepository;
    @Autowired UserManagementServiceImpl userManagementService;
    @Autowired VoteServiceImpl voteService;
    @Autowired MercenaryTransientRepository mercenaryTransientRepository;

    @DisplayName("Member 등록 승인 테스트")
    @Test
    @Transactional
    void memberApproveTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "testMember",
                "01012341234",
                Gender.MALE,
                birth,
                "by0398467!@",
                1L,
                1L
        );
        City city = cityRepository.findById(1L).orElseThrow();
        District district = districtRepository.findById(1L).orElseThrow();
        MemberEntity member = memberRepository.save(
                MemberEntity.create(signup, city, district)
        );

        log.info("Before approval member status : {}", member.getStatus());
        assertEquals(member.getStatus(), MemberStatus.PENDING);
        userManagementService.approveMember(member.getId());

        log.info("After approval member status : {}", member.getStatus());
        assertEquals(member.getStatus(), MemberStatus.ENABLED);
    }


    @DisplayName("Mercenary 등록 승인 테스트")
    @Test
    @Transactional
    void mercenaryApproveTest() {
        MercenaryRegistrationRequest registration = new MercenaryRegistrationRequest(
                "testName",
                "01012341234",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryEntity mercenary = mercenaryRepository.save(
                MercenaryEntity.create(registration)
        );

        LocalDate now = LocalDate.now();

        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                MatchType.REGULAR,
                now
        );
        MemberEntity admin = memberRepository.findByMobile("01043053451").get();
        voteService.createMatchVote(admin.getId(), matchVoteCreateRequest);
        MatchVoteEntity matchVote = matchVoteRepository.findByMatchAt(now).get(0);

        MatchEntity match = matchRepository.findByMatchVote(matchVote).orElseGet(() -> null);
        assertNotNull(match);
        log.info("Before approve mercenary status : {}", mercenary.getStatus());
        userManagementService.approveMercenary(mercenary.getId(), match.getId());

        MercenaryTransientEntity transientEntity = mercenaryTransientRepository.findByMercenary(mercenary).orElseGet(() -> null);

        assertNotNull(transientEntity);

        log.info("transientEntity code : {}", transientEntity.getCode());

        assertEquals(transientEntity.getMatch().getId(), match.getId());

        log.info("transientEntity matchId : {}", transientEntity.getMatch().getId());
        log.info("original matchId : {}", match.getId());

        assertEquals(mercenary.getStatus(), MercenaryStatus.ENABLED);
        log.info("After approve mercenary status : {}", mercenary.getStatus());
    }


}
