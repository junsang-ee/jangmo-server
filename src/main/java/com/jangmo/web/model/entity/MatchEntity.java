package com.jangmo.web.model.entity;

import com.jangmo.web.constants.match.MatchStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.model.entity.common.CreationTimestampEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;


@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match")
public class MatchEntity extends CreationTimestampEntity {

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private MemberEntity createdBy;


}

