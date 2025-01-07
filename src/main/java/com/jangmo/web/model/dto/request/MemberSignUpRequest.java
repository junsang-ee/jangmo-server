package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import com.jangmo.web.constants.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MemberSignUpRequest {

    @NotBlank(message = "이름은 필수 항목입니다.")
    @ValidFields(field = "name")
    private final String name;

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @ValidFields(field = "mobile")
    private final String mobile;

    @NotNull(message = "성별은 필수 항목입니다.")
    private final Gender gender;

    @Past(message = "생년월일은 현재보다 과거 날짜여야 합니다.")
    @NotNull(message = "생년월일은 필수 항목입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birth;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @ValidFields(field = "password")
    private final String password;

    @NotNull(message = "시/도는 필수 항목입니다.")
    private final Long cityId;

    @NotNull(message = "시/군/구는 필수 항목입니다.")
    private final Long districtId;

}
