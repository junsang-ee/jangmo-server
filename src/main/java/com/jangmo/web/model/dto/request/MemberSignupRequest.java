package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;

import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
