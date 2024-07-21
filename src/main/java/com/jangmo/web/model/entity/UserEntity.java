package com.jangmo.web.model.entity;

import com.jangmo.web.config.UuidGenerator;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "user")
public class UserEntity extends UuidGenerator {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MobileCarrierType mobileCarrier;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private int birth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;



}
