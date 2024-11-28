package com.jangmo.web.model.entity.user;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.UserStatus;
import com.jangmo.web.model.entity.common.CreationTimestampEntity;

import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;
import static javax.persistence.InheritanceType.JOINED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@Table(name = "\"user\"")
@Entity(name = "user")
public class UserEntity extends CreationTimestampEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mobile;

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


    protected UserEntity(String name, String mobile,
                         MobileCarrierType mobileCarrier,
                         UserRole role, Gender gender) {
        this.name = name;
        this.mobile = mobile;
        this.mobileCarrier = mobileCarrier;
        this.role = role;
        this.gender = gender;
        this.status = UserStatus.PENDING;
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }
}
