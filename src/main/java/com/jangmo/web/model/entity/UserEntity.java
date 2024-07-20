package com.jangmo.web.model.entity;

import com.jangmo.web.config.UuidGenerator;
import com.jangmo.web.constants.UserRole;
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
    private String name;

    private String number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String birth;

}
