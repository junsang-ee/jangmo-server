package com.jangmo.web.model.entity.vote.user;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.SequentialEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@MappedSuperclass
public abstract class AbstractVoteUserEntity extends SequentialEntity {

    @Column(nullable = false)
    private String voterId;

    @Column(nullable = false)
    private String voterName;

    @Column(nullable = false)
    private UserRole voterRole;

    protected AbstractVoteUserEntity(final UserEntity voter) {
        this.voterId = voter.getId();
        this.voterName = voter.getName();
        this.voterRole = voter.getRole();
    }



}
