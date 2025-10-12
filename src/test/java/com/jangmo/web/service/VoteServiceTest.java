package com.jangmo.web.service;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.vote.MatchVoteOption;
import com.jangmo.web.constants.vote.VoteModeType;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCastRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.vote.user.MatchVoteUserEntity;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.user.MemberRepository;
import com.jangmo.web.repository.vote.MatchVoteRepository;
import com.jangmo.web.repository.vote.user.MatchVoteUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class VoteServiceTest {

    @Autowired MatchVoteRepository matchVoteRepository;
    @Autowired MatchVoteUserRepository matchVoteUserRepository;
    @Autowired CityRepository cityRepository;
    @Autowired DistrictRepository districtRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired VoteServiceImpl voteService;

    static final String CAST_MATCH_VOTE = "매치 투표 테스트";

    @Value("${jangmo.admin.name}")
    String adminName;

    @Value("${jangmo.admin.password}")
    String adminPassword;

    @Value("${jangmo.admin.mobile}")
    String adminMobile;

    MemberEntity admin;
    City city;
    District district;

    @BeforeEach
    void init(TestInfo testInfo) {
        String display = testInfo.getDisplayName();
        switch (display) {
            case CAST_MATCH_VOTE:
                initCityAndDistrict();
                createAdmin();
            default: break;
        }
    }

    void initCityAndDistrict() {
        city = cityRepository.save(City.of("서울특별시"));
        district = districtRepository.save(District.of("종로구", city));
        assertNotNull(cityRepository.findByName("서울특별시"));
        assertNotNull(districtRepository.findByCityAndName(city, "종로구"));
        assertNotNull(city.getId());
        assertNotNull(district.getId());
    }

    void createAdmin() {
        MemberSignUpRequest adminSignup = new MemberSignUpRequest(
                adminName,
                adminMobile,
                Gender.MALE,
                LocalDate.of(1994, 3, 16),
                adminPassword,
                1L, 1L
        );
        admin = MemberEntity.create(adminSignup, city, district);
        memberRepository.save(admin);

        assertNotNull(admin.getId());
        assertNotNull(memberRepository.findById(admin.getId()));
    }

    @DisplayName(CAST_MATCH_VOTE)
    @Test
    void castMatchVoteTest() {
        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                "Test Title",
                MatchType.FUTSAL,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(1),
                VoteModeType.SINGLE
        );
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest(
                "황테스트",
                "01012341111",
                Gender.MALE,
                LocalDate.of(1994, 3, 16),
                "1231231!",
                1L,
                1L
        );
        MemberEntity testMember = MemberEntity.create(
                memberSignUpRequest, city, district
        );
        memberRepository.save(testMember);
        List<UserEntity> voters = Arrays.asList(admin, testMember);

        MatchVoteEntity matchVote = MatchVoteEntity.create(
            admin, matchVoteCreateRequest, voters
        );

        matchVoteRepository.save(matchVote);

        assertNotNull(matchVote.getId());
        assertNotNull(matchVoteRepository.findById(matchVote.getId()));

        MatchVoteCastRequest matchVoteCastRequest = new MatchVoteCastRequest(
                MatchVoteOption.ANTE_MERIDIEM
        );
        voteService.castMatchVote(
                matchVote.getId(),
                admin.getId(),
                matchVoteCastRequest
        );

        MatchVoteUserEntity votedVoter = matchVoteUserRepository.findByMatchVoteAndVoterId(
                matchVote, admin.getId()
        ).orElse(null);

        MatchVoteUserEntity notVotedVoter = matchVoteUserRepository.findByMatchVoteAndVoterId(
                matchVote, testMember.getId()
        ).orElse(null);

        assertNotNull(votedVoter);
        assertNotNull(notVotedVoter);
        assertEquals(MatchVoteOption.ANTE_MERIDIEM, votedVoter.getMatchVoteOption());
        assertEquals(MatchVoteOption.NOT_VOTED, notVotedVoter.getMatchVoteOption());
        log.info("admin(voted) voteOption : {}", votedVoter.getMatchVoteOption());
        log.info("testMember(notVoted) voteOption : {}", notVotedVoter.getMatchVoteOption());
    }

}
