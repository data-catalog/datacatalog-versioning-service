package edu.bbte.projectbluebook.datacatalog.versioning.controller;

import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.ErrorResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.exception.NotFoundException;
import edu.bbte.projectbluebook.datacatalog.versioning.exception.VersionServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e,
                                                                         ServerHttpRequest request) {
        String message =  e.getConstraintViolations()
                .stream()
                .map(it -> it.getPropertyPath().toString() + " " + it.getMessage())
                .collect(Collectors.joining(", "));

        return constructError(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                            ServerHttpRequest request) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(it -> it.getField() + " " + it.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return constructError(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e,
                                                                       ServerHttpRequest request) {
        return constructError(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(VersionServiceException.class)
    public final ResponseEntity<ErrorResponse> handleVersionServiceException(VersionServiceException e,
                                                                             ServerHttpRequest request) {
        return constructError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> constructError(HttpStatus status, String message,
                                                           ServerHttpRequest request) {
        ErrorResponse body = new ErrorResponse()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getURI().getPath());

        return new ResponseEntity<>(body, status);
    }
}
