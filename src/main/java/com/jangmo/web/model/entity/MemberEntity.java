package com.jangmo.web.model.entity;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.dto.request.MemberSignupRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "member")
public class MemberEntity extends AbstractUserEntity implements Serializable {

    @Column(nullable = false)
    private int birth;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Builder
    protected MemberEntity(String name, int phoneNumber,
                           MobileCarrierType mobileCarrier,
                           UserRole role, Gender gender,
                           int birth, String password,
                           String address) {
        super(name, phoneNumber, mobileCarrier, role, gender);
        this.birth = birth;
        this.password = password;
        this.address = address;
    }

    public static MemberEntity create(final MemberSignupRequest signUp) {
        return MemberEntity.builder()
                .name(signUp.getName())
                .phoneNumber(signUp.getPhoneNumber())
                .mobileCarrier(signUp.getMobileCarrier())
                .role(signUp.getRole())
                .gender(signUp.getGender())
                .birth(signUp.getBirth())
                .password(signUp.getPassword())
                .address(signUp.getAddress())
                .build();
    }

}
