package com.jangmo.web.service;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.constants.vote.MatchVoteOption;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.vote.MatchVoteCastRequest;
import com.jangmo.web.model.dto.response.vote.MatchVoteCastResponse;
import com.jangmo.web.model.dto.response.vote.UserMatchVoteStatusResponse;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.vote.user.MatchVoteUserEntity;
import com.jangmo.web.repository.vote.MatchVoteRepository;
import com.jangmo.web.repository.vote.user.MatchVoteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

    private final MatchVoteRepository matchVoteRepository;

    private final MatchVoteUserRepository matchVoteUserRepository;

    @Override
    @Transactional
    public MatchVoteCastResponse castMatchVote(String matchVoteId, String userId, MatchVoteCastRequest request) {
        MatchVoteEntity matchVote = getMatchVote(matchVoteId);
        MatchVoteUserEntity voter = getMatchVoteUser(matchVote, userId);
        MatchVoteOption selectedOption = request.getOption();
        voter.updateOption(selectedOption);
        return MatchVoteCastResponse.of(selectedOption);
    }

    @Override
    public UserMatchVoteStatusResponse getMatchVoteStatus(String matchVoteId, String userId) {
        MatchVoteEntity matchVote = getMatchVote(matchVoteId);
        MatchVoteUserEntity voter = getMatchVoteUser(matchVote, userId);
        boolean isVoted = voter.getMatchVoteOption() != MatchVoteOption.NOT_VOTED;
        return UserMatchVoteStatusResponse.of(
                isVoted, voter.getMatchVoteOption()
        );
    }

    private MatchVoteEntity getMatchVote(String matchVoteId) {
        return matchVoteRepository.findById(matchVoteId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MATCH_VOTE_NOT_FOUND)
        );
    }

    private MatchVoteUserEntity getMatchVoteUser(MatchVoteEntity matchVote, String userId) {
        return matchVoteUserRepository.findByMatchVoteAndVoterId(
                matchVote, userId
        ).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MATCH_VOTE_USER_NOT_FOUND)
        );
    }
}