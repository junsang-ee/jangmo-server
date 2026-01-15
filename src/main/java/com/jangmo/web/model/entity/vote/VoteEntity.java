package com.jangmo.web.model.entity.vote;

import com.jangmo.web.constants.vote.VoteModeType;
import com.jangmo.web.constants.vote.VoteType;
import com.jangmo.web.model.entity.ReplyTargetEntity;

import com.jangmo.web.model.entity.user.MemberEntity;

import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@Entity(name = "vote")
public class VoteEntity extends ReplyTargetEntity {

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private LocalDate startAt;

	@Column(nullable = false)
	private LocalDate endAt;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VoteModeType modeType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VoteType voteType;

	protected VoteEntity(
		MemberEntity createdBy,
		String title,
		LocalDate startAt,
		LocalDate endAt,
		VoteModeType modeType,
		VoteType voteType
	) {
		super(createdBy);
		this.title = title;
		this.startAt = startAt;
		this.endAt = endAt;
		this.modeType = modeType;
		this.voteType = voteType;
	}

}
