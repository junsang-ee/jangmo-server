package com.jangmo.web.model.entity.vote;

import com.jangmo.web.constants.VoteType;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.vote.user.MatchVoteUserEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString(exclude = {"match", "voters"})
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_vote")
public class MatchVoteEntity extends VoteEntity {

    @Column(nullable = false)
    private LocalDate matchAt;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "match", nullable = false)
    private MatchEntity match;

    @OneToMany(mappedBy = "matchVote",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<MatchVoteUserEntity> voters;

    private MatchVoteEntity(MemberEntity createdBy,
                            MatchVoteCreateRequest request,
                            List<UserEntity> rawVoters) {
        super(
                createdBy,
                request.getTitle(),
                LocalDate.now(),
                request.getEndAt(),
                request.getSelectionType(),
                VoteType.MATCH
        );
        this.matchAt = request.getMatchAt();
        this.match = MatchEntity.create(
                createdBy,
                request.getMatchAt(),
                request.getMatchType(),
                this
        );
        this.voters = MatchVoteUserEntity.createAll(
                rawVoters, this
        );
    }

    public static MatchVoteEntity create(final MemberEntity createdBy,
                                         final MatchVoteCreateRequest request,
                                         final List<UserEntity> rawVoters) {
        return new MatchVoteEntity(createdBy, request, rawVoters);
    }

    public void setVoters(List<MatchVoteUserEntity> voters) {
        this.voters = voters;
    }

    public void addVoter(MatchVoteUserEntity voter) {
        voters.add(voter);
    }

}
