package com.jangmo.web.model.entity.vote.user;

import com.jangmo.web.constants.vote.MatchVoteOption;
import com.jangmo.web.model.entity.user.UserEntity;

import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;


@Getter
@ToString(exclude = {"matchVote"})
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_vote_user")
public class MatchVoteUserEntity extends AbstractVoteUserEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "match_vote", nullable = false)
	private MatchVoteEntity matchVote;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MatchVoteOption matchVoteOption;

	private MatchVoteUserEntity(
		UserEntity voter, MatchVoteEntity matchVote
	) {
		super(voter);
		this.matchVote = matchVote;
		this.matchVoteOption = MatchVoteOption.NOT_VOTED;
	}

	public static MatchVoteUserEntity create(
		final UserEntity rawVoter, final MatchVoteEntity matchVote
	) {
		return new MatchVoteUserEntity(rawVoter, matchVote);
	}

	public static List<MatchVoteUserEntity> createAll(
		final List<UserEntity> rawVoters, final MatchVoteEntity matchVote
	) {
		return rawVoters.stream().map(
			rawVoter -> create(rawVoter, matchVote)
		).collect(Collectors.toList());
	}

	public void updateOption(MatchVoteOption option) {
		this.matchVoteOption = option;
	}
}
