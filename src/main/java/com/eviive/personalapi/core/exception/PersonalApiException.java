package com.eviive.personalapi.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PersonalApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final boolean logException;

    public PersonalApiException(
        final Throwable cause,
        final String message,
        final HttpStatus httpStatus,
        final boolean logException
    ) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.logException = logException;
    }

    public PersonalApiException(final String message, final HttpStatus httpStatus, final boolean logException) {
        this(null, message, httpStatus, logException);
    }

    public PersonalApiException(final String message, final HttpStatus httpStatus) {
        this(null, message, httpStatus, true);
    }

    public PersonalApiException(final PersonalApiErrorsEnum personalApiErrorsEnum) {
        this(
            personalApiErrorsEnum.getMessage(),
            personalApiErrorsEnum.getHttpStatus(),
            personalApiErrorsEnum.isLogException()
        );
    }

    // Formatted messages

    public static PersonalApiException format(
        final Throwable cause,
        final PersonalApiErrorsEnum personalApiErrorsEnum,
        final Object... args
    ) {
        return new PersonalApiException(
            cause,
            personalApiErrorsEnum.getMessage().formatted(args),
            personalApiErrorsEnum.getHttpStatus(),
            personalApiErrorsEnum.isLogException()
        );
    }

    public static PersonalApiException format(
        final PersonalApiErrorsEnum personalApiErrorsEnum,
        final Object... args
    ) {
        return format(null, personalApiErrorsEnum, args);
    }

}
