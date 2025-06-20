package com.jangmo.web.model.entity.user;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.UserRole;

import com.jangmo.web.model.ModificationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import static lombok.AccessLevel.PROTECTED;
import static javax.persistence.InheritanceType.JOINED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@Table(name = "\"user\"")
@Entity(name = "user")
public class UserEntity extends ModificationTimestampEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    protected UserEntity(String name, String mobile,
                         UserRole role, Gender gender) {
        this.name = name;
        this.mobile = mobile;
        this.role = role;
        this.gender = gender;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

}

