package com.jangmo.web.model.entity.vote.user;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.ModificationTimestampEntity;
import com.jangmo.web.model.entity.user.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@MappedSuperclass
public abstract class AbstractVoteUserEntity extends ModificationTimestampEntity {

    @Column(nullable = false)
    private String voterId;

    @Column(nullable = false)
    private String voterName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole voterRole;

    protected AbstractVoteUserEntity(final UserEntity voter) {
        this.voterId = voter.getId();
        this.voterName = voter.getName();
        this.voterRole = voter.getRole();
    }



}
