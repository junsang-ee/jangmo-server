package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import com.jangmo.web.constants.AuthPurposeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeVerifyRequest {

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @ValidFields(field = "mobile")
    private String mobile;

    @NotBlank(message = "인증 코드는 필수 항목입니다.")
    @ValidFields(field = "authCode")
    private String code;

    @NotNull(message = "인증 타입은 필수 항목입니다.")
    private AuthPurposeType authPurposeType;
}
