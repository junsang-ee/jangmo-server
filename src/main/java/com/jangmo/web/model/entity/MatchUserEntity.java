package com.jangmo.web.model.entity;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MatchParticipationStatus;
import com.jangmo.web.constants.UserRole;

import jakarta.persistence.*;

import com.jangmo.web.model.CreationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity(name = "match_user")
@NoArgsConstructor(access = PROTECTED)
public class MatchUserEntity extends CreationTimestampEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "match_detail", nullable = false)
	private MatchDetailEntity matchDetail;

	@Column(nullable = false)
	private String playerId;

	@Column(nullable = false)
	private String playerName;

	@Column(nullable = false)
	private UserRole playerRole;

	@Column(nullable = false)
	private Gender playerGender;

	@Enumerated(EnumType.STRING)
	private MatchParticipationStatus participationStatus;

}
