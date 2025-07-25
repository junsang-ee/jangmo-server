package com.jangmo.web.config.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, PARAMETER })
@Constraint(validatedBy = ValidFieldsImpl.class)
public @interface ValidFields {

    String message() default "";
    String field();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
