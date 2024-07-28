package com.jangmo.web.exception.custom;

import com.jangmo.web.constants.message.ErrorMessage;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.core.NestedRuntimeException;

@Getter
public class BaseException extends NestedRuntimeException {
    @Accessors(fluent = true)
    private ErrorMessage error;

    public BaseException(String reason) {
        super(reason);
    }

    public BaseException(ErrorMessage error) {
        super(error.code());
        this.error = error;
    }
}
