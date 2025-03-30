package com.jangmo.web.model.entity;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MatchParticipationStatus;
import com.jangmo.web.constants.UserRole;

import com.jangmo.web.model.CreationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_user")
public class MatchUserEntity extends CreationTimestampEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match", nullable = false)
    private MatchEntity match;

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
