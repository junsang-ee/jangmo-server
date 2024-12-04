package com.jangmo.web.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerificationRequest {

    private String mobile;

    private String code;
}
