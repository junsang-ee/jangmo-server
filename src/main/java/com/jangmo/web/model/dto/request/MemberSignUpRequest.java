package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequest {

    @NotNull
    private String name;

    @NotNull
    private String mobile;

    @NotNull
    private MobileCarrierType mobileCarrier;

    @NotNull
    private Gender gender;

    @NotNull
    private int birth;

    @NotNull
    private String password;

    @NotNull
    private String address;

    @NotNull
    private UserRole role;

}
