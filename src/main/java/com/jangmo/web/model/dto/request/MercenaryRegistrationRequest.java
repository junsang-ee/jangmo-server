package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MercenaryRegistrationRequest {

    @NotNull
    private String name;

    @NotNull
    private String mobile;

    @NotNull
    private MobileCarrierType mobileCarrier;

    @NotNull
    private UserRole role;

    @NotNull
    private Gender gender;

    @NotNull
    private MercenaryRetentionStatus retentionStatus;

}
