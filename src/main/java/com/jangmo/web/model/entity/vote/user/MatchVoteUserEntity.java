package com.jangmo.web.model.entity.vote.user;

import com.jangmo.web.constants.vote.MatchVoteStatus;
import com.jangmo.web.model.entity.user.UserEntity;

import com.jangmo.web.model.entity.vote.MatchVoteEntity;
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

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;


@Getter
@ToString(exclude = {"matchVote"})
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_vote_user")
public class MatchVoteUserEntity extends AbstractVoteUserEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_vote", nullable = false)
    private MatchVoteEntity matchVote;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchVoteStatus matchVoteStatus;

    private MatchVoteUserEntity(UserEntity voter,
                                MatchVoteEntity matchVote) {
        super(voter);
        this.matchVote = matchVote;
        this.matchVoteStatus = MatchVoteStatus.NOT_VOTED;
    }

    public static MatchVoteUserEntity create(final UserEntity rawVoter,
                                             final MatchVoteEntity matchVote) {
        return new MatchVoteUserEntity(rawVoter, matchVote);
    }

    public static List<MatchVoteUserEntity> createAll(final List<UserEntity> rawVoters,
                                                      final MatchVoteEntity matchVote) {
        return rawVoters.stream().map(
                rawVoter -> create(rawVoter, matchVote)
        ).collect(Collectors.toList());
    }
}
