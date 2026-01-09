package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import com.jangmo.web.constants.Gender;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequest {

    @NotBlank(message = "이름은 필수 항목입니다.")
    @ValidFields(field = "name")
    private String name;

    @Pattern(
        regexp = "^010-\\d{4}-\\d{4}$",
        message = "휴대폰 번호 형식이 올바르지 않습니다."
    )
    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    private String mobile;

    @NotNull(message = "성별은 필수 항목입니다.")
    private Gender gender;

    @Past(message = "생년월일은 현재보다 과거 날짜여야 합니다.")
    @NotNull(message = "생년월일은 필수 항목입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @ValidFields(field = "password")
    private String password;

    @NotNull(message = "시/도는 필수 항목입니다.")
    private Long cityId;

    @NotNull(message = "시/군/구는 필수 항목입니다.")
    private Long districtId;

}
