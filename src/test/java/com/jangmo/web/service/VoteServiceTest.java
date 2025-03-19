package com.jangmo.web.service;

import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.MatchVoteCreateResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.MatchRepository;
import com.jangmo.web.repository.MatchVoteRepository;
import com.jangmo.web.repository.UserRepository;
import com.jangmo.web.service.manager.VoteServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class VoteServiceTest {

    @Autowired VoteServiceImpl voteService;

    @Autowired UserRepository userRepository;

    @Autowired MatchVoteRepository matchVoteRepository;

    @Autowired MatchRepository matchRepository;


    @DisplayName("match 투표 생성 테스트")
    @Test
    @Transactional
    void matchVoteCreateTest() {
        UserEntity admin = userRepository.findByMobile("01043053451").orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
        );
        LocalDate now = LocalDate.now();
        LocalDate matchAt = now.plusDays(2);
        LocalDate endAt = now.plusDays(1);
        MatchVoteCreateRequest createRequest = new MatchVoteCreateRequest(
                MatchType.REGULAR,
                matchAt,
                endAt
        );
        MatchVoteCreateResponse response = voteService.createMatchVote(admin.getId(), createRequest);
        List<MatchVoteEntity> matchVoteList = matchVoteRepository.findByMatchAt(now);
        log.info("matchVoteList size: " + matchVoteList.size());
        MatchVoteEntity matchVote = matchVoteList.get(0);

        MatchEntity match = matchRepository.findByMatchVote(matchVote).orElseGet(() -> null);
        assertNotNull(response);
        assertNotNull(matchVote);
        assertNotNull(match);
        assertEquals(matchVote.getMatch().getId(), match.getId());
        log.info("matchVote getMatchId : " + matchVote.getMatch().getId());
        log.info("matchId : " + match.getId());
        log.info("response matchAt : " + response.getMatchAt());
        log.info("now : " + now);
        log.info("response name : " + response.getCreatorName());
        log.info("admin name : " + admin.getName());
        assertEquals(response.getMatchAt(), now);
        assertEquals(response.getCreatorName(), admin.getName());
    }
}
