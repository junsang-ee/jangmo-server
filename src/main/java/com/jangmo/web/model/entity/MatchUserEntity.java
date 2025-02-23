package com.jangmo.web.model.entity;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MatchParticipationStatus;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.entity.common.CreationTimestampEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

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
