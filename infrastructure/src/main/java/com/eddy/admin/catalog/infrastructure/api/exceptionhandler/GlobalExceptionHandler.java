package com.eddy.admin.catalog.infrastructure.api.exceptionhandler;

import com.eddy.admin.catalog.domain.exceptions.DomainException;
import com.eddy.admin.catalog.domain.exceptions.NotFoundException;
import com.eddy.admin.catalog.domain.validation.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(ex));
    }

    record ApiError(String message, List<Error> errors) {
        static ApiError from(DomainException ex) {
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}
