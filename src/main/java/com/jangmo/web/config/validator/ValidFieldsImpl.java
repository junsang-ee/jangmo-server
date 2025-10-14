package com.jangmo.web.config.validator;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.constants.vote.MatchVoteOption;
import com.jangmo.web.exception.FieldValidationException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidFieldsImpl implements ConstraintValidator<ValidFields, Object> {

    private final static String NAME_REGEX = "^[가-힣]{2,}$";
    private final static String PASSWORD_REGEX =
            "^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\\d~!@#$%^&*()_+=]{8,}$";

    private final static String MOBILE_REGEX = "^\\d{11}$";

    private final static String MERCENARY_CODE_REGEX = "^[A-Za-z0-9]{10}$";

    private final static String VALID_CODE_REGEX = "^\\d{6}$";
    private String fieldName;

    @Override
    public void initialize(ValidFields constraintAnnotation) {
        fieldName = constraintAnnotation.field();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return false;

        switch (fieldName) {
            case "mobile": validMobile(String.valueOf(value)); break;
            case "password": validPassword(String.valueOf(value)); break;
            case "name": validName(String.valueOf(value)); break;
            case "authCode": validAuthCode(String.valueOf(value)); break;
            case "mercenaryCode": validMercenaryCode(String.valueOf(value)); break;
            case "matchVoteOption": validMatchVoteOption(value);
            default: break;
        }

        return true;
    }

    private void validMobile(String mobile) {
        if (!mobile.matches(MOBILE_REGEX))
            throwFieldException(ErrorMessage.REQUEST_INVALID_MOBILE);
    }

    private void validPassword(String password) {
        if (!password.matches(PASSWORD_REGEX))
            throwFieldException(ErrorMessage.REQUEST_INVALID_PASSWORD);
    }

    private void validName(String name) {
        if (!name.matches(NAME_REGEX))
            throwFieldException(ErrorMessage.REQUEST_INVALID_NAME);
    }

    private void validAuthCode(String code) {
        if (!code.matches(VALID_CODE_REGEX))
            throwFieldException(ErrorMessage.REQUEST_INVALID_CODE);
    }

    private void validMercenaryCode(String mercenaryCode) {
        if (!mercenaryCode.matches(MERCENARY_CODE_REGEX))
            throwFieldException(ErrorMessage.REQUEST_INVALID_MERCENARY_CODE);
    }


    private void validMatchVoteOption(Object value) {
        MatchVoteOption enumValue = getEnumValue(value, MatchVoteOption.class);
        if (enumValue == MatchVoteOption.NOT_VOTED)
            throw new FieldValidationException(ErrorMessage.MATCH_VOTE_INVALID_OPTION);
    }

    private <E extends Enum<E>> E getEnumValue(Object value, Class<E> enumClass) {
        try {
            return Enum.valueOf(enumClass, value.toString());
        } catch (IllegalArgumentException e) {
            throwFieldException(ErrorMessage.REQUEST_BODY_FIELD);
        }
        return null;
    }

    private void throwFieldException(ErrorMessage message) {
        throw new FieldValidationException(message);
    }
}
