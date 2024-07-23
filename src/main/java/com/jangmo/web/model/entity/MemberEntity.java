package com.jangmo.web.model.entity;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.dto.request.MemberSignupRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "member")
public class MemberEntity extends AbstractUserEntity {

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

    public static MemberEntity of(final MemberSignupRequest signup) {
        return MemberEntity.builder()
                .name(signup.getName())
                .phoneNumber(signup.getPhoneNumber())
                .mobileCarrier(signup.getMobileCarrier())
                .role(signup.getRole())
                .gender(signup.getGender())
                .birth(signup.getBirth())
                .password(signup.getPassword())
                .address(signup.getAddress())
                .build();
    }

}
