package dev.albertv.projects.api.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProjectsApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final boolean logException;

    public ProjectsApiException(
        final Throwable cause,
        final String message,
        final HttpStatus httpStatus,
        final boolean logException
    ) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.logException = logException;
    }

    public ProjectsApiException(final String message, final HttpStatus httpStatus, final boolean logException) {
        this(null, message, httpStatus, logException);
    }

    public ProjectsApiException(final String message, final HttpStatus httpStatus) {
        this(null, message, httpStatus, true);
    }

    public ProjectsApiException(final ProjectsApiErrorsEnum projectsApiErrorsEnum) {
        this(
            projectsApiErrorsEnum.getMessage(),
            projectsApiErrorsEnum.getHttpStatus(),
            projectsApiErrorsEnum.isLogException()
        );
    }

    // Formatted messages

    public static ProjectsApiException format(
        final Throwable cause,
        final ProjectsApiErrorsEnum projectsApiErrorsEnum,
        final Object... args
    ) {
        return new ProjectsApiException(
            cause,
            projectsApiErrorsEnum.getMessage().formatted(args),
            projectsApiErrorsEnum.getHttpStatus(),
            projectsApiErrorsEnum.isLogException()
        );
    }

    public static ProjectsApiException format(
        final ProjectsApiErrorsEnum projectsApiErrorsEnum,
        final Object... args
    ) {
        return format(null, projectsApiErrorsEnum, args);
    }

}
