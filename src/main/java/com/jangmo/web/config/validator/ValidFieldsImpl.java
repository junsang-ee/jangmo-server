package com.jangmo.web.config.validator;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.FieldValidationException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidFieldsImpl implements ConstraintValidator<ValidFields, String> {

    private final static String NAME_REGEX = "^[가-힣]{2,}$";
    private final static String PASSWORD_REGEX =
            "^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\\d~!@#$%^&*()_+=]{8,}$";

    private final static String MOBILE_REGEX = "^\\d{11}$";

    private final static String MERCENARY_CODE_REGEX = "^[A-Za-z0-9!@#$%^&*()\\-_=+]{10}$";

    private final static String VALID_CODE_REGEX = "^\\d{6}$";
    private String fieldName;

    @Override
    public void initialize(ValidFields constraintAnnotation) {
        fieldName = constraintAnnotation.field();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        switch (fieldName) {
            case "mobile": validMobile(value); break;
            case "password": validPassword(value); break;
            case "name": validName(value); break;
            case "validCode": validCode(value); break;
            case "mercenaryCode": validMercenaryCode(value); break;
            default: break;
        }

        return true;
    }

    private void validMobile(String mobile) {
        if (!mobile.matches(MOBILE_REGEX))
            throw new FieldValidationException(ErrorMessage.REQUEST_INVALID_MOBILE);
    }

    private void validPassword(String password) {
        if (!password.matches(PASSWORD_REGEX))
            throw new FieldValidationException(ErrorMessage.REQUEST_INVALID_PASSWORD);
    }

    private void validName(String name) {
        if (!name.matches(NAME_REGEX))
            throw new FieldValidationException(ErrorMessage.REQUEST_INVALID_NAME);
    }

    private void validCode(String code) {
        if (!code.matches(VALID_CODE_REGEX))
            throw new FieldValidationException(ErrorMessage.REQUEST_INVALID_CODE);
    }

    private void validMercenaryCode(String mercenaryCode) {
        if (!mercenaryCode.matches(MERCENARY_CODE_REGEX))
            throw new FieldValidationException(ErrorMessage.REQUEST_INVALID_MERCENARY_CODE);
    }
}
