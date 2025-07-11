package com.eviive.personalapi.core.exception;

import com.eviive.personalapi.util.ErrorUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.ClientAbortException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API400_MISSING_SERVLET_REQUEST_PARAMETER;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API400_TYPE_MISMATCH;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API401_UNAUTHORIZED;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API403_FORBIDDEN;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API408_REQUEST_TIMEOUT;
import static com.eviive.personalapi.core.exception.PersonalApiErrorsEnum.API500_INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestControllerAdvice
@RequiredArgsConstructor
public class PersonalApiExceptionHandler extends ResponseEntityExceptionHandler
    implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final String NEW_LINE = "\n";

    private final ErrorUtilities errorUtilities;

    private final ObjectMapper objectMapper;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ProblemDetail> handleAllExceptions(
        final Exception e,
        final WebRequest req
    ) {
        final ProblemDetail errorResponse;
        boolean defaultExceptionHandler = false;
        boolean logException = true;

        switch (e) {
            case PersonalApiException personalApiException:
                errorResponse = errorUtilities.buildError(personalApiException);
                logException = personalApiException.isLogException();
                break;
            case JpaSystemException jpaSystemException:
                errorResponse = errorUtilities.buildError(
                    API500_INTERNAL_SERVER_ERROR,
                    jpaSystemException.getMostSpecificCause().getMessage()
                );
                break;
            case TransactionSystemException transactionSystemException:
                errorResponse = errorUtilities.buildError(
                    API500_INTERNAL_SERVER_ERROR,
                    transactionSystemException.getMostSpecificCause().getMessage()
                );
                break;
            case NestedRuntimeException nestedRuntimeException:
                errorResponse = errorUtilities.buildError(
                    API500_INTERNAL_SERVER_ERROR,
                    nestedRuntimeException.getMostSpecificCause().getMessage()
                );
                break;
            case ClientAbortException clientAbortException:
                errorResponse = errorUtilities.buildError(
                    API408_REQUEST_TIMEOUT,
                    clientAbortException.getLocalizedMessage()
                );
                logException = false;
                break;
            default:
                errorResponse = errorUtilities.buildError(API500_INTERNAL_SERVER_ERROR, e.getMessage());
                defaultExceptionHandler = true;
                break;
        }

        if (logException) {
            final String loggerMessage = "%s: %s".formatted(
                getExceptionHandlerName(e, defaultExceptionHandler),
                req.getDescription(false)
            );

            if (HttpStatusCode.valueOf(errorResponse.getStatus()).is5xxServerError()) {
                logger.error(loggerMessage, e);
            } else {
                logger.warn(loggerMessage, e);
            }
        }

        return ResponseEntity.status(errorResponse.getStatus())
            .body(errorResponse);
    }

    private String getExceptionHandlerName(
        final Exception e,
        final boolean defaultExceptionHandler
    ) {
        return (defaultExceptionHandler ? "DefaultExceptionHandler (%s)" : "%sHandler").formatted(
            e.getClass().getSimpleName()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException e,
        @NotNull final HttpHeaders headers,
        @NotNull final HttpStatusCode status,
        @NotNull final WebRequest req
    ) {
        return handleBadRequestException(
            e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(NEW_LINE))
        );
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
        @NotNull final HandlerMethodValidationException ex,
        @NotNull final HttpHeaders headers,
        @NotNull final HttpStatusCode status,
        @NotNull final WebRequest request
    ) {
        return handleBadRequestException(
            ex.getParameterValidationResults()
                .stream()
                .flatMap(r ->
                    r.getResolvableErrors()
                        .stream()
                        .map(e -> "The %s parameter %s.".formatted(
                            r.getMethodParameter().getParameterName(),
                            e.getDefaultMessage()
                        ))
                )
                .collect(Collectors.joining(NEW_LINE))
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        final MissingServletRequestParameterException e,
        @NotNull final HttpHeaders headers,
        @NotNull final HttpStatusCode status,
        @NotNull final WebRequest req
    ) {
        return handleBadRequestException(
            API400_MISSING_SERVLET_REQUEST_PARAMETER,
            e.getParameterName(),
            e.getParameterType()
        );
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
        final TypeMismatchException e,
        @NotNull final HttpHeaders headers,
        @NotNull final HttpStatusCode status,
        @NotNull final WebRequest req
    ) {
        return handleBadRequestException(API400_TYPE_MISMATCH, e.getPropertyName());
    }

    @Override
    public void commence(
        final HttpServletRequest req,
        final HttpServletResponse res,
        final AuthenticationException authException
    ) {
        sendError(res, API401_UNAUTHORIZED);
    }

    @Override
    public void handle(
        final HttpServletRequest req,
        final HttpServletResponse res,
        final AccessDeniedException accessDeniedException
    ) {
        sendError(res, API403_FORBIDDEN);
    }

    private ResponseEntity<Object> handleBadRequestException(final String detail) {
        return ResponseEntity.badRequest()
            .body(errorUtilities.buildError(BAD_REQUEST, detail));
    }

    private ResponseEntity<Object> handleBadRequestException(
        final PersonalApiErrorsEnum personalApiErrorsEnum,
        final Object... args
    ) {
        return handleBadRequestException(personalApiErrorsEnum.getMessage().formatted(args));
    }

    private void sendError(final HttpServletResponse res, final PersonalApiErrorsEnum personalApiErrorsEnum) {
        final ProblemDetail errorResponse = errorUtilities.buildError(personalApiErrorsEnum);

        res.setStatus(errorResponse.getStatus());
        res.setContentType(APPLICATION_JSON_VALUE);
        try {
            res.getOutputStream().print(objectMapper.writeValueAsString(errorResponse));
            res.flushBuffer();
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

}
