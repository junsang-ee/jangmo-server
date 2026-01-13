package com.jangmo.web.service.manager;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.vote.GeneralVoteCreateRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.request.vote.VoteListRequest;
import com.jangmo.web.model.dto.response.vote.GeneralVoteCreateResponse;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.vote.VoteListResponse;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.vote.GeneralVoteEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.user.UserEntity;

import com.jangmo.web.repository.user.MemberRepository;
import com.jangmo.web.repository.user.UserRepository;
import com.jangmo.web.repository.vote.GeneralVoteRepository;
import com.jangmo.web.repository.vote.MatchVoteRepository;
import com.jangmo.web.repository.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoteManagementServiceImpl implements VoteManagementService {

	private final UserRepository userRepository;

	private final MemberRepository memberRepository;

	private final VoteRepository voteRepository;
	private final MatchVoteRepository matchVoteRepository;
	private final GeneralVoteRepository generalVoteRepository;

	@Override
	@Transactional
	public MatchVoteCreateResponse createMatchVote(String userId, MatchVoteCreateRequest request) {
		MemberEntity createdBy = getMemberId(userId);
		List<UserEntity> rawVoters = getRawMatchVoters();
		MatchVoteEntity matchVote = MatchVoteEntity.create(createdBy, request, rawVoters);
		matchVoteRepository.save(matchVote);
		return MatchVoteCreateResponse.of(matchVote);
	}

	@Override
	@Transactional
	public GeneralVoteCreateResponse createGeneralVote(String userId, GeneralVoteCreateRequest request) {
		MemberEntity createdBy = getMemberById(userId);
		List<UserEntity> rawVoters = getRawGeneralVoters();
		GeneralVoteEntity generalVote = GeneralVoteEntity.create(
			createdBy, request, rawVoters
		);
		generalVoteRepository.save(generalVote);
		return GeneralVoteCreateResponse.of(
			generalVote.getStartAt(),
			generalVote.getEndAt(),
			generalVote.getCreatedBy().getName()
		);
	}

	@Override
	public List<VoteListResponse> getVotes(VoteListRequest request) {
		return voteRepository.findVotes(request);
	}

	private List<UserEntity> getRawGeneralVoters() {
		List<MemberStatus> statuses = Arrays.asList(
			MemberStatus.DISABLED, MemberStatus.PENDING
		);
		return userRepository.findUserByMemberStatusNotAndRole(
			statuses, UserRole.MEMBER
		);
	}


	private MemberEntity getMemberId(String memberId) {
		return memberRepository.findById(memberId).orElseThrow(
			() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
		);
	}

	private MemberEntity getMemberById(String memberId) {
		return memberRepository.findById(memberId).orElseThrow(
			() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
		);
	}

	private List<UserEntity> getRawMatchVoters() {
		return userRepository.findUserByMemberStatusAndRoleNot(
			MemberStatus.ENABLED,
			UserRole.MERCENARY
		);
	}

}
