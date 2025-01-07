package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class MemberLoginRequest {

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @ValidFields(field = "mobile")
    private final String mobile;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @ValidFields(field = "password")
    private final String password;
}
