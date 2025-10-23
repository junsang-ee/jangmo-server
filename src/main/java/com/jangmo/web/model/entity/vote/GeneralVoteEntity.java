package com.jangmo.web.model.entity.vote;

import com.jangmo.web.constants.vote.VoteType;
import com.jangmo.web.model.dto.request.vote.GeneralVoteCreateRequest;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.model.entity.vote.user.GeneralVoteUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "general_vote")
public class GeneralVoteEntity extends VoteEntity {

    @OneToMany(mappedBy = "generalVote",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<GeneralVoteUserEntity> voters;

    private GeneralVoteEntity(MemberEntity createdBy,
                              GeneralVoteCreateRequest request,
                              List<UserEntity> rawVoters) {
        super(
                createdBy,
                request.getTitle(),
                LocalDate.now(),
                request.getEndAt(),
                request.getModeType(),
                VoteType.GENERAL
        );
        this.voters = GeneralVoteUserEntity.createAll(
                rawVoters, this
        );

    }

    public static GeneralVoteEntity create(final MemberEntity createdBy,
                                           final GeneralVoteCreateRequest request,
                                           final List<UserEntity> rawVoters) {
        return new GeneralVoteEntity(createdBy, request, rawVoters);
    }



}
