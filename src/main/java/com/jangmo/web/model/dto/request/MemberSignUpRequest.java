package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import com.jangmo.web.constants.Gender;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequest {

    @NotBlank(message = "이름은 필수 항목입니다.")
    @ValidFields(field = "name")
    private String name;

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @ValidFields(field = "mobile")
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
