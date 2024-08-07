package com.jangmo.web.model.entity;

import com.jangmo.web.model.entity.common.CreationTimeStampEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_vote_user")
public class MatchVoteUserEntity extends CreationTimeStampEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mercenary", nullable = false)
    private MercenaryEntity mercenary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_vote", nullable = false)
    private MatchVoteEntity matchVote;


}
