package com.jangmo.web.model.entity;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.UserStatus;
import com.jangmo.web.model.CreationTimeStampEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@MappedSuperclass
public abstract class AbstractUserEntity extends CreationTimeStampEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MobileCarrierType mobileCarrier;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    protected AbstractUserEntity(String name, int phoneNumber,
                                 MobileCarrierType mobileCarrier,
                                 UserRole role, Gender gender) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mobileCarrier = mobileCarrier;
        this.role = role;
        this.gender = gender;
        this.status = UserStatus.PENDING;
    }

}
