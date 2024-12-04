package com.jangmo.web.model.entity;

import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
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
public class MatchVoteEntity extends CreationUserEntity {

    @Column(nullable = false)
    private LocalDate matchAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match", nullable = false)
    private MatchEntity match;

    @OneToMany(mappedBy = "matchVote",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<MatchVoteUserEntity> voters;

    private MatchVoteEntity(UserEntity writer,
                            MatchVoteCreateRequest request) {
        super(writer);
        this.matchAt = request.getMatchAt();
    }

    public static MatchVoteEntity create(final UserEntity writer,
                                         final MatchVoteCreateRequest request) {
        return new MatchVoteEntity(writer, request);
    }

    public void addVoter(MatchVoteUserEntity voter) {
        voters.add(voter);
    }

}
