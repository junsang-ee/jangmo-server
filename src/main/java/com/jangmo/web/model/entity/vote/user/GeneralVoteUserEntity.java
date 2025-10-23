package com.jangmo.web.model.entity.vote.user;

import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.model.entity.vote.GeneralVoteEntity;
import com.jangmo.web.model.entity.vote.GeneralVoteSelectionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString(exclude = {"generalVote"})
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "general_vote_user")
public class GeneralVoteUserEntity extends AbstractVoteUserEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_vote",nullable = false)
    private GeneralVoteEntity generalVote;

    @OneToMany(mappedBy = "generalVoteUser",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<GeneralVoteSelectionEntity> selectedOptions;

    private GeneralVoteUserEntity(UserEntity rawVoter,
                                  GeneralVoteEntity generalVote) {
        super(rawVoter);
        this.generalVote = generalVote;
        this.selectedOptions = new ArrayList<>();
    }

    public static GeneralVoteUserEntity create(final UserEntity rawVoter,
                                               final GeneralVoteEntity generalVote) {
        return new GeneralVoteUserEntity(rawVoter, generalVote);
    }

    public static List<GeneralVoteUserEntity> createAll(final List<UserEntity> rawVoters,
                                                        final GeneralVoteEntity generalVote) {
        return rawVoters.stream().map(
                rawVoter -> create(rawVoter, generalVote)
        ).collect(Collectors.toList());
    }
}
