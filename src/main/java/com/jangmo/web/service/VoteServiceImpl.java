package com.jangmo.web.service;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.vote.MatchVoteCastRequest;
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
    public void castMatchVote(String matchVoteId, String userId, MatchVoteCastRequest request) {
        MatchVoteEntity matchVote = matchVoteRepository.findById(matchVoteId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MATCH_VOTE_NOT_FOUND)
        );

        MatchVoteUserEntity voter = matchVoteUserRepository.findByMatchVoteAndVoterId(
                matchVote, userId
        ).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MATCH_VOTE_USER_NOT_FOUND)
        );
        voter.updateOption(request.getOption());
    }
}