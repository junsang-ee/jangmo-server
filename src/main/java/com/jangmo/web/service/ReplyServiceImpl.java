package com.jangmo.web.service;

import com.jangmo.web.constants.ReplyTargetType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.board.ReplyCreateRequest;
import com.jangmo.web.model.dto.response.board.ReplyCreateResponse;
import com.jangmo.web.model.entity.CommentTargetEntity;
import com.jangmo.web.model.entity.board.ReplyEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.MatchVoteRepository;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.VoteRepository;
import com.jangmo.web.repository.board.PostRepository;
import com.jangmo.web.repository.board.ReplyRepository;
import com.jangmo.web.repository.board.ReplyTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    private final MatchVoteRepository matchVoteRepository;

    private final VoteRepository voteRepository;

    private final ReplyTargetRepository replyTargetRepository;

    @Override
    @Transactional
    public ReplyCreateResponse create(String memberId,
                                      String targetId,
                                      ReplyTargetType targetType,
                                      ReplyCreateRequest request) {
        MemberEntity createdBy = getMemberById(memberId);

        validateTarget(targetId, targetType);
        CommentTargetEntity commentTarget = replyTargetRepository.findById(targetId).orElseThrow(
        );
        ReplyEntity reply = ReplyEntity.create(
                createdBy,
                commentTarget,
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

    private void validateTarget(String targetId, ReplyTargetType targetType) {
        switch (targetType) {
            case GENERAL_VOTE:
                voteRepository.findById(targetId).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.VOTE_NOT_FOUND)
                );
                break;
            case MATCH_VOTE:
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
