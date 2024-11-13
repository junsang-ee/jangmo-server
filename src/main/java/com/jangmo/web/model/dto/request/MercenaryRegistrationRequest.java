package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MercenaryRegistrationRequest {

    @NotNull
    private String name;

    @NotNull
    private int mobile;

    @NotNull
    private MobileCarrierType mobileCarrier;

    @NotNull
    private UserRole role;

    @NotNull
    private Gender gender;

    @NotNull
    private MercenaryRetentionStatus retentionStatus;

}
