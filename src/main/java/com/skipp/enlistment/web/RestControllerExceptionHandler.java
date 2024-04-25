package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.EnlistmentException;
import com.skipp.enlistment.domain.RecordAlreadyExistsException;
import com.skipp.enlistment.domain.RecordNotFoundException;
import com.skipp.enlistment.domain.ReferentialIntegrityViolationException;
import com.skipp.enlistment.domain.SectionCreationException;
import com.skipp.enlistment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Used for handling exceptions and returning the correct response status code.
 */
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // Hint: What do the tests expect?
    public ErrorResponse handleInvalidArguments(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EnlistmentException.class)

    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    // Hint: What do the tests expect?
    public ErrorResponse handleEnlistmentException(EnlistmentException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ReferentialIntegrityViolationException.class)
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    // Hint: What do the tests expect?
    public ErrorResponse handleReferentialIntegrityViolationException(ReferentialIntegrityViolationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(SectionCreationException.class)
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    // Hint: What do the tests expect?
    public ErrorResponse handleSectionCreationException(SectionCreationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.CONFLICT)
    // Hint: What do the tests expect?
    public ErrorResponse handleDuplicateKeys(RecordAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(RecordNotFoundException.class)
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.NOT_FOUND)
    // Hint: What do the tests expect?
    public ErrorResponse handleRecordNotFoundException(RecordNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.FORBIDDEN)
    // Hint: What do the tests expect?
    public ErrorResponse handleAccessDenied(AccessDeniedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // Hint: This is a catch-all for all other unexpected errors. This means it's a _server_ issue, not a client one.
    public ErrorResponse handleOtherErrors(Throwable e) {
        e.printStackTrace();
        return new ErrorResponse(e.getMessage());
    }

}
