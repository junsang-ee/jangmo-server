package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberUpdatePasswordRequest {

    @NotBlank(message = "현재 비밀번호는 필수 항목입니다.")
    @ValidFields(field = "password")
    private String oldPassword;

    @NotBlank(message = "새로운 비밀번호는 필수 항목입니다.")
    @ValidFields(field = "password")
    private String newPassword;
}
