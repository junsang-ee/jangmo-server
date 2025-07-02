package com.jangmo.web.model.entity.vote;

import com.jangmo.web.model.ModificationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;


@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "general_vote_option")
public class GeneralVoteOptionEntity extends ModificationTimestampEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_vote_id", nullable = false)
    private GeneralVoteEntity generalVote;

    @Column(nullable = false)
    private String option;

    private GeneralVoteOptionEntity(GeneralVoteEntity generalVote, String option) {
        this.generalVote = generalVote;
        this.option = option;
    }

    public static GeneralVoteOptionEntity create(final GeneralVoteEntity generalVote,
                                                 final String option) {
        return new GeneralVoteOptionEntity(generalVote, option);
    }
}
