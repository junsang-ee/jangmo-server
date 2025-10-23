package com.jangmo.web.model.entity.user;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.UserRole;

import com.jangmo.web.model.ModificationTimestampEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
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

