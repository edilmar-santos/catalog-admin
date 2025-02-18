package com.eddy.admin.catalog.infrastructure.api.controllers;

import com.eddy.admin.catalog.domain.exceptions.DomainException;
import com.eddy.admin.catalog.domain.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    record ApiError(String message, List<Error> errors) {
        static ApiError from(DomainException ex) {
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}
