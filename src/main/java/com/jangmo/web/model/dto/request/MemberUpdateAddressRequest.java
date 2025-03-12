package com.jangmo.web.model.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberUpdateAddressRequest {

    @NotNull
    private Long cityId;

    @NotNull
    private Long districtId;

}
