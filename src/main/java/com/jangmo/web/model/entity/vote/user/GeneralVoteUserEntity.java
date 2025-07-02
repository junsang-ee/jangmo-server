package com.jangmo.web.model.entity.vote.user;

import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.model.entity.vote.GeneralVoteEntity;
import com.jangmo.web.model.entity.vote.GeneralVoteOptionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString(exclude = {"generalVote"})
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "general_vote_user")
public class GeneralVoteUserEntity extends AbstractVoteUserEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_vote", nullable = false)
    private GeneralVoteEntity generalVote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option", nullable = false)
    private GeneralVoteOptionEntity selectedOption;

    private GeneralVoteUserEntity(UserEntity rawVoter,
                                  GeneralVoteEntity generalVote) {
        super(rawVoter);
        this.generalVote = generalVote;
        this.selectedOption = null;
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
