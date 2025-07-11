package com.eviive.personalapi.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@RequiredArgsConstructor
@Getter
@SuppressWarnings("checkstyle:LineLength")
public enum PersonalApiErrorsEnum {

    // 400 Bad Request

    API400_REFRESH_TOKEN_NOT_FOUND("No refresh token was found in the request.", BAD_REQUEST, false),
    API400_FILE_EMPTY("The received file is empty.", BAD_REQUEST),
    API400_IMAGE_NO_NAME("The received image has no name.", BAD_REQUEST),
    API400_MISSING_SERVLET_REQUEST_PARAMETER("The request parameter %s of type %s is missing.", BAD_REQUEST),
    API400_TYPE_MISMATCH("The received type for the property %s is not valid.", BAD_REQUEST),
    API400_PROJECT_ID_NOT_ALLOWED("The project id must be null for a new project.", BAD_REQUEST),
    API400_SKILL_ID_NOT_ALLOWED("The skill id must be null for a new skill.", BAD_REQUEST),

    // 401 Unauthorized

    API401_UNAUTHORIZED("You are not authorized to access this resource.", UNAUTHORIZED),
    API401_LOGIN_FAILED("Login failed: %s.", UNAUTHORIZED),
    API401_TOKEN_ERROR("An error occurred while processing the token: %s.", UNAUTHORIZED),

    // 403 Forbidden

    API403_FORBIDDEN("You are not allowed to access this resource.", FORBIDDEN),

    // 404 Not Found

    API404_USERNAME_NOT_FOUND("The user with username %s was not found.", NOT_FOUND),
    API404_PROJECT_ID_NOT_FOUND("Project with id %d was not found.", NOT_FOUND),
    API404_SKILL_ID_NOT_FOUND("Skill with id %d was not found.", NOT_FOUND),

    // 415 Unsupported Media Type

    API415_FILE_NOT_IMAGE("The received file is not an image (Content-Type: %s).", UNSUPPORTED_MEDIA_TYPE),

    // 500 Internal Server Error

    API500_INTERNAL_SERVER_ERROR("An internal server error occurred: %s.", INTERNAL_SERVER_ERROR),
    API500_UPLOAD_ERROR("An error occurred while uploading the image: %s.", INTERNAL_SERVER_ERROR),
    API500_UNKNOWN_CONTAINER("The image %s is not linked to a known container.", INTERNAL_SERVER_ERROR);

    private final String message;

    private final HttpStatus httpStatus;

    private final boolean logException;

    PersonalApiErrorsEnum(final String message, final HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.logException = true;
    }

}
