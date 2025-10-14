package com.jangmo.web.service;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.vote.MatchVoteOption;
import com.jangmo.web.constants.vote.VoteModeType;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCastRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.vote.UserMatchVoteStatusResponse;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    static final String MATCH_VOTE_STATUS = "매치 투표 상태 체크 테스트";

    @Value("${jangmo.admin.name}")
    String adminName;

    @Value("${jangmo.admin.password}")
    String adminPassword;

    @Value("${jangmo.admin.mobile}")
    String adminMobile;

    MemberEntity admin;
    MemberEntity testMember;
    City city;
    District district;

    @BeforeEach
    void init(TestInfo testInfo) {
        String display = testInfo.getDisplayName();
        switch (display) {
            case CAST_MATCH_VOTE:
            case MATCH_VOTE_STATUS:
                initCityAndDistrict();
                createAdmin();
                createTestMember();
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

    void createTestMember() {
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "김철수",
                "01012341111",
                Gender.MALE,
                LocalDate.of(1994, 3, 16),
                "1231231!",
                1L,
                1L
        );
        testMember = MemberEntity.create(
                signup, city, district
        );
        memberRepository.save(testMember);
        assertNotNull(testMember.getId());
        assertNotNull(memberRepository.findById(testMember.getId()));
    }

    MatchVoteEntity getSavedMatchVote() {
        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                "Test Title",
                MatchType.FUTSAL,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(1),
                VoteModeType.SINGLE
        );
        List<UserEntity> rawVoters = Arrays.asList(admin, testMember);
        MatchVoteEntity matchVote = MatchVoteEntity.create(
                admin, matchVoteCreateRequest, rawVoters
        );
        matchVoteRepository.save(matchVote);
        assertNotNull(matchVote.getId());
        assertNotNull(matchVoteRepository.findById(matchVote.getId()));
        return matchVote;
    }

    @DisplayName(CAST_MATCH_VOTE)
    @Test
    void castMatchVoteTest() {
        MatchVoteEntity matchVote = getSavedMatchVote();
        MatchVoteCastRequest matchVoteCastRequest = new MatchVoteCastRequest(
                MatchVoteOption.ANTE_MERIDIEM
        );
        voteService.castMatchVote(
                matchVote.getId(),
                testMember.getId(),
                matchVoteCastRequest
        );

        MatchVoteUserEntity votedVoter = matchVoteUserRepository.findByMatchVoteAndVoterId(
                matchVote, testMember.getId()
        ).orElse(null);

        MatchVoteUserEntity netVotedVoter = matchVoteUserRepository.findByMatchVoteAndVoterId(
                matchVote, admin.getId()
        ).orElse(null);

        assertNotNull(votedVoter);
        assertNotNull(netVotedVoter);
        assertEquals(MatchVoteOption.ANTE_MERIDIEM, votedVoter.getMatchVoteOption());
        log.info("testMember (voted) voteOption : {}", votedVoter.getMatchVoteOption());
        log.info("Admin (NotVoted) voteOption : {}", netVotedVoter.getMatchVoteOption());
    }

    @DisplayName(MATCH_VOTE_STATUS)
    @Test
    void getMatchVoteStatusTest() {

        MatchVoteEntity matchVote = getSavedMatchVote();

        String testMemberId = testMember.getId();

        MatchVoteUserEntity voter = matchVoteUserRepository.findByMatchVoteAndVoterId(
                matchVote, testMemberId
        ).orElse(null);

        assertNotNull(voter);

        voter.updateOption(MatchVoteOption.ABSENT);

        UserMatchVoteStatusResponse response = voteService.getMatchVoteStatus(
                matchVote.getId(), testMemberId
        );

        assertTrue(response.isVoted());
        assertEquals(MatchVoteOption.ABSENT, response.getSelectedOption());
        log.info("Voter's isVoted : {}", response.isVoted());
        log.info("Voter's selectedOption : {}", response.getSelectedOption());
    }

}

