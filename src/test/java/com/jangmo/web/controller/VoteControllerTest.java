package com.jangmo.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.vote.MatchVoteOption;
import com.jangmo.web.model.dto.request.vote.MatchVoteCastRequest;
import com.jangmo.web.model.dto.response.vote.MatchVoteCastResponse;
import com.jangmo.web.security.ExtendedUserDetails;
import com.jangmo.web.service.VoteService;
import com.jangmo.web.utils.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(VoteController.class)
public class VoteControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private VoteService voteService;

    @MockBean private MessageUtil messageUtil;

    @MockBean private MessageSource messageSource;

    ExtendedUserDetails principal;

    final static String CAST_MATCH_VOTE_SUCCESS = "매치 투표 API 성공 테스트";
    final static String CAST_MATCH_VOTE_PARAM_ERROR = "매치 투표 API 요청 파라미터 실패 테스트";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(MessageUtil.class, "messageSource", messageSource);
        createPrincipal();
    }

    void createPrincipal() {
        String testUserId = "testUserId";
        principal = new ExtendedUserDetails(
                testUserId,
                "01012341111",
                UserRole.MEMBER
        );

    }

    @DisplayName(CAST_MATCH_VOTE_SUCCESS)
    @Test
    void castMatchVoteSuccessTest() throws Exception {
        //given
        String matchVoteId = "testMatchVoteId";
        String userId = "testUserId";

        MatchVoteCastRequest castMatchVoteRequest =
                new MatchVoteCastRequest(MatchVoteOption.ABSENT);

        MatchVoteCastResponse castMatchVoteResponse =
                MatchVoteCastResponse.of(MatchVoteOption.ABSENT);

        when(voteService.castMatchVote(eq(matchVoteId), eq(userId), any(MatchVoteCastRequest.class)))
                .thenReturn(castMatchVoteResponse);

        // when & then
        mockMvc.perform(post("/api/votes/matches/{matchVoteId}", matchVoteId)
                        .with(csrf())
                        .with(user(principal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(castMatchVoteRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(voteService, times(1))
                .castMatchVote(eq(matchVoteId), eq(userId), any(MatchVoteCastRequest.class));

    }

    @DisplayName(CAST_MATCH_VOTE_PARAM_ERROR)
    @Test
    void castMatchVoteParamErrorTest() throws Exception {
        //given
        String matchVoteId = "testMatchVoteId";
        String userId = "testUserId";

        MatchVoteCastRequest castMatchVoteRequest =
                new MatchVoteCastRequest(MatchVoteOption.NOT_VOTED);

        MatchVoteCastResponse castMatchVoteResponse =
                MatchVoteCastResponse.of(MatchVoteOption.NOT_VOTED);

        when(voteService.castMatchVote(eq(matchVoteId), eq(userId), any(MatchVoteCastRequest.class)))
                .thenReturn(castMatchVoteResponse);

        // when & then
        mockMvc.perform(post("/api/votes/matches/{matchVoteId}", matchVoteId)
                        .with(csrf())
                        .with(user(principal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(castMatchVoteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(5032))
                .andDo(print());

    }
}
