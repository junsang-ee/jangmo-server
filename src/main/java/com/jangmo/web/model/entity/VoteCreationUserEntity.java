package com.jangmo.web.model.entity;

import com.jangmo.web.model.entity.common.ModificationTimestampEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class VoteCreationUserEntity extends ModificationTimestampEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private MemberEntity createdBy;

    protected VoteCreationUserEntity(final MemberEntity user) {
        this.createdBy = user;
    }
}
