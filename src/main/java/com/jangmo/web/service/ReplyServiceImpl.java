package com.jangmo.web.service;

import com.jangmo.web.constants.ReplyTargetType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.board.ReplyCreateRequest;
import com.jangmo.web.model.dto.response.board.ReplyCreateResponse;
import com.jangmo.web.model.entity.board.ReplyEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.MatchRepository;
import com.jangmo.web.repository.MatchVoteRepository;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.board.PostRepository;
import com.jangmo.web.repository.board.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    private final MatchRepository matchRepository;

    private final MatchVoteRepository matchVoteRepository;

    @Override
    @Transactional
    public ReplyCreateResponse create(String memberId, ReplyCreateRequest request) {
        MemberEntity createdBy = getMemberById(memberId);
        validateTargetExists(request.getTargetType(), request.getTargetId());
        ReplyEntity reply = ReplyEntity.create(
                createdBy,
                request
        );
        replyRepository.save(reply);
        return ReplyCreateResponse.of(reply);
    }

    private MemberEntity getMemberById(String memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
    }

    private void validateTargetExists(ReplyTargetType target, String targetId) {
        switch (target) {
            case MATCH:
                matchRepository.findById(targetId).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.MATCH_NOT_FOUND)
                );
                break;
            case VOTE:
                matchVoteRepository.findById(targetId).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.MATCH_VOTE_NOT_FOUND)
                );
                break;
            case POST:
                postRepository.findById(targetId).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
                );
                break;
        }
    }

}
