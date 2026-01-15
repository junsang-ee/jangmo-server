package com.jangmo.web.model.entity.user;

import com.jangmo.web.model.SequentialEntity;
import com.jangmo.web.model.entity.MatchEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Entity(name = "mercenary_transient")
public class MercenaryTransientEntity extends SequentialEntity {

	@Column(nullable = false)
	private String code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "match", nullable = false)
	private MatchEntity match;

	public static MercenaryTransientEntity create(
		final String encodedCode,
		final MatchEntity match
	) {
		return new MercenaryTransientEntity(
			encodedCode,
			match
		);
	}

	public void updateCode(String encodedCode) {
		this.code = encodedCode;
	}
}
