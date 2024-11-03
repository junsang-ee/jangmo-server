package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import lombok.*;

@Getter
@NoArgsConstructor
public class MemberSignupRequest {

    private String name;

    private int mobile;

    private MobileCarrierType mobileCarrier;

    private Gender gender;

    private int birth;

    private String password;

    private String address;

    private UserRole role;

    @Builder
    public MemberSignupRequest(String name, UserRole role, int birth, Gender gender,
                               MobileCarrierType mobileCarrier, String address,
                               int mobile, String password) {
        this.name = name;
        this.role = role;
        this.birth = birth;
        this.gender = gender;
        this.mobileCarrier = mobileCarrier;
        this.address = address;
        this.mobile = mobile;
        this.password = password;
    }

}
