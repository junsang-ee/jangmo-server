package com.jangmo.web.model.dto.request;

import com.jangmo.web.config.validator.ValidFields;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MobileRequest {

    @ValidFields(field = "mobile")
    private String mobile;
}
