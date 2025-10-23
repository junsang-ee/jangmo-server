package com.jangmo.web.model.entity;

import com.jangmo.web.constants.match.MatchStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match")
public class MatchEntity extends CreationUserEntity {

    @Column(nullable = false)
    private LocalDate matchAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_vote")
    private MatchVoteEntity matchVote;

    @OneToOne(cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "match_detail")
    private MatchDetailEntity matchDetail;

    private MatchEntity(MemberEntity createdBy,
                        LocalDate matchAt,
                        MatchType type,
                        MatchVoteEntity matchVote) {
        super(createdBy);
        this.matchAt = matchAt;
        this.type = type;
        this.matchVote = matchVote;
        this.status = MatchStatus.PENDING;
        this.matchDetail = null;
    }

    public static MatchEntity create(final MemberEntity createdBy,
                                     final LocalDate matchAt,
                                     final MatchType type,
                                     final MatchVoteEntity matchVote) {
        return new MatchEntity(createdBy, matchAt, type, matchVote);
    }

}

