package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MercenaryRegistrationRequest {

    private String name;

    private int phoneNumber;

    private MobileCarrierType mobileCarrier;

    private UserRole role;

    private Gender gender;
}
