package com.jangmo.web.model.entity;

import com.jangmo.web.model.ModificationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity(name = "match_detail")
@NoArgsConstructor(access = PROTECTED)
public class MatchDetailEntity extends ModificationTimestampEntity {

	@Column(nullable = false)
	private LocalDateTime startAt;

	@Column(nullable = false)
	private LocalDateTime endAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ground", nullable = false)
	private GroundEntity ground;

	@OneToMany(mappedBy = "matchDetail", cascade = CascadeType.REMOVE)
	private List<MatchUserEntity> players;

}
