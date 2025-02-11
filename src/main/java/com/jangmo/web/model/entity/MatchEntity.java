package com.jangmo.web.model.entity;

import com.jangmo.web.constants.match.MatchStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

//import javax.persistence.*;

import javax.persistence.*;

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

    private MatchEntity(UserEntity user, LocalDate matchAt, MatchType type) {
        super(user);
        this.matchAt = matchAt;
        this.type = type;
        this.status = MatchStatus.PENDING;
    }

    public static MatchEntity create(final UserEntity user,
                                     final LocalDate matchAt,
                                     final MatchType type) {
        return new MatchEntity(user, matchAt, type);
    }
}

