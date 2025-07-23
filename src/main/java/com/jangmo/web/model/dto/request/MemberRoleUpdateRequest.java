package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberRoleUpdateRequest {

    @NotBlank(message = "바뀔 유저 권한은 필수 항목입니다.")
    private UserRole role;

}
