package com.jangmo.web.model.dto.response.common;

import com.jangmo.web.constants.message.SuccessMessage;
import com.jangmo.web.utils.MessageUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Getter
@ToString(callSuper = true)
public class ApiSuccessResponse<T> extends ApiResponse {

    private final T data;

    public ApiSuccessResponse(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public static <T> ApiSuccessResponse<T> wrap(T data) {
        boolean hasResult =
                (data instanceof PageResponse && ((PageResponse<?>) data).getTotalCount() > 0) ||
                        (data instanceof List && !((List<?>) data).isEmpty()) ||
                        (data != null && !(data instanceof List) && !(data instanceof PageResponse));

        SuccessMessage success = hasResult ? SuccessMessage.RESULT : SuccessMessage.EMPTY;
        return new ApiSuccessResponse<>(
                success.getCode(), MessageUtil.getMessage(success.resName()), data
        );
    }

    public static <T> ApiSuccessResponse<PageResponse<T>> page(Page<T> data) {
        return wrap(new PageResponse<>(data));
    }

    public static <T> ApiSuccessResponse<T> optional(Optional<T> data) {
        return data.map(ApiSuccessResponse::wrap).orElseGet(() -> wrap(null));
    }
}
