package com.jangmo.web.model.entity;

import com.jangmo.web.model.ModificationTimestampEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class CreationUserEntity extends ModificationTimestampEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by",
            nullable = false,
            updatable = false)
    private MemberEntity createdBy;

    protected CreationUserEntity(final MemberEntity createdBy) {
        this.createdBy = createdBy;
    }

}