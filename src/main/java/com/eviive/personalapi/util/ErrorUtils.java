package com.eviive.personalapi.util;

import com.eviive.personalapi.core.exception.PersonalApiErrorsEnum;
import com.eviive.personalapi.core.exception.PersonalApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public final class ErrorUtils {

    public ProblemDetail buildError(final HttpStatus httpStatus, final String detail) {
        return ProblemDetail.forStatusAndDetail(
            httpStatus,
            detail
        );
    }

    public ProblemDetail buildError(
        final PersonalApiErrorsEnum personalApiErrorsEnum,
        final Object... args
    ) {
        return this.buildError(
            personalApiErrorsEnum.getHttpStatus(),
            personalApiErrorsEnum.getMessage().formatted(args)
        );
    }

    public ProblemDetail buildError(final PersonalApiException personalApiException) {
        return this.buildError(
            personalApiException.getHttpStatus(),
            personalApiException.getMessage()
        );
    }

}
