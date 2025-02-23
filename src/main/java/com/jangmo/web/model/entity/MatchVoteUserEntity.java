package com.jangmo.web.model.entity;

import com.jangmo.web.constants.MatchVoteStatus;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.entity.common.CreationTimestampEntity;
import com.jangmo.web.model.entity.user.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString(exclude = {"matchVote"})
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_vote_user")
public class MatchVoteUserEntity extends CreationTimestampEntity {

    @Column(nullable = false)
    private String voterId;

    @Column(nullable = false)
    private String voterName;

    @Column(nullable = false)
    private UserRole voterRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_vote", nullable = false)
    private MatchVoteEntity matchVote;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchVoteStatus matchVoteStatus;

    private MatchVoteUserEntity(UserEntity voter,
                                MatchVoteEntity matchVote) {
        this.voterId = voter.getId();
        this.voterName = voter.getName();
        this.voterRole = voter.getRole();
        this.matchVote = matchVote;
        this.matchVoteStatus = MatchVoteStatus.NOT_VOTED;
    }

    public static MatchVoteUserEntity create(final UserEntity voter,
                                             final MatchVoteEntity matchVote) {
        return new MatchVoteUserEntity(
                voter,
                matchVote
        );
    }
}
