package com.jangmo.web.model.entity;

import com.jangmo.web.constants.MatchVoteStatus;
import com.jangmo.web.model.entity.common.CreationTimestampEntity;

import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString(exclude = {"voter", "matchVote"})
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_vote_user")
public class MatchVoteUserEntity extends CreationTimestampEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter", nullable = false)
    private UserEntity voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_vote", nullable = false)
    private MatchVoteEntity matchVote;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchVoteStatus matchVoteStatus;

    private MatchVoteUserEntity(UserEntity voter,
                                MatchVoteEntity matchVote) {
        this.voter = voter;
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
