package com.jangmo.web.model.entity.vote;

import com.jangmo.web.model.CreationTimestampEntity;
import com.jangmo.web.model.entity.vote.user.GeneralVoteUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "general_vote_selection")
public class GeneralVoteSelectionEntity extends CreationTimestampEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "general_vote_user", nullable = false)
	private GeneralVoteUserEntity generalVoteUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "selected_option", nullable = false)
	private GeneralVoteOptionEntity selectedOption;

	private GeneralVoteSelectionEntity(
		GeneralVoteUserEntity generalVoteUser,
		GeneralVoteOptionEntity selectedOption
	) {
		this.generalVoteUser = generalVoteUser;
		this.selectedOption = selectedOption;
	}

	public static GeneralVoteSelectionEntity create(
		final GeneralVoteUserEntity generalVoteUser,
		final GeneralVoteOptionEntity selectedOption
	) {
		return new GeneralVoteSelectionEntity(generalVoteUser, selectedOption);
	}

}
