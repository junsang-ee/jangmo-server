package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSignupRequest {

    private String name;

    private int phoneNumber;

    private MobileCarrierType mobileCarrier;

    private Gender gender;

    private int birth;

    private String password;

    private String address;

    private UserRole role;

}
