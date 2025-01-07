package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class MercenaryRegistrationRequest {

    @NotBlank(message = "이름은 필수 항목입니다.")
    @ValidFields(field = "name")
    private final String name;

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @ValidFields(field = "mobile")
    private final String mobile;

    @NotNull(message = "성별은 필수 항목입니다.")
    private final Gender gender;

    @NotNull
    private final MercenaryRetentionStatus retentionStatus;

}
