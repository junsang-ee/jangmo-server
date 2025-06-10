package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @ValidFields(field = "mobile")
    private String mobile;

    @NotBlank(message = "새로운 비밀번호는 필수 항목입니다.")
    @ValidFields(field = "password")
    private String newPassword;

}
