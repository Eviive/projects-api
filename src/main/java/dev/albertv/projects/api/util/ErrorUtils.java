package dev.albertv.projects.api.util;

import dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum;
import dev.albertv.projects.api.core.exception.ProjectsApiException;
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
        final ProjectsApiErrorsEnum projectsApiErrorsEnum,
        final Object... args
    ) {
        return this.buildError(
            projectsApiErrorsEnum.getHttpStatus(),
            projectsApiErrorsEnum.getMessage().formatted(args)
        );
    }

    public ProblemDetail buildError(final ProjectsApiException projectsApiException) {
        return this.buildError(
            projectsApiException.getHttpStatus(),
            projectsApiException.getMessage()
        );
    }

}
