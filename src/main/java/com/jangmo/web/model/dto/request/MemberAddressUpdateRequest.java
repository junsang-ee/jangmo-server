package com.jangmo.web.model.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberAddressUpdateRequest {

    @NotNull(message = "시/도는 필수 항목입니다.")
    private Long cityId;

    @NotNull(message = "시/군/구는 필수 항목입니다.")
    private Long districtId;

}
