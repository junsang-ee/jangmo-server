package com.jangmo.web.model.entity;

import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString(exclude = {"match", "voters"})
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_vote")
public class MatchVoteEntity extends CreationUserEntity {

    @Column(name = "match_at", nullable = false)
    private LocalDate matchAt;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "match", nullable = false)
    private MatchEntity match;

    @OneToMany(mappedBy = "matchVote",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<MatchVoteUserEntity> voters;

    private MatchVoteEntity(UserEntity user,
                            MatchVoteCreateRequest request) {
        super(user);
        this.matchAt = request.getMatchAt();
        this.match = MatchEntity.create(
                user,
                request.getMatchAt(),
                request.getMatchType(),
                this
        );
    }

    public static MatchVoteEntity create(final UserEntity createdBy,
                                         final MatchVoteCreateRequest request) {
        return new MatchVoteEntity(createdBy, request);
    }

    public void setVoters(List<MatchVoteUserEntity> voters) {
        this.voters = voters;
    }

    public void addVoter(MatchVoteUserEntity voter) {
        voters.add(voter);
    }

}
