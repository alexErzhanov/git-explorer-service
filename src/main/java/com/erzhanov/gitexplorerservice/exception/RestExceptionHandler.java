package com.erzhanov.gitexplorerservice.exception;

import com.erzhanov.gitexplorerservice.dto.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    public static final String GITHUB_USER_NOT_FOUND = "Github user not found";

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(FeignException.NotFound ex) {
        log.warn("Thrown exception: {}", ex.getMessage());
        var errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(GITHUB_USER_NOT_FOUND)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidMediaTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMediaTypeException(InvalidMediaTypeException ex) {
        log.warn("An exception occurred: {}", ex.getMessage());
        ErrorResponse errorResponse =
                ErrorResponse.builder()
                        .status(HttpStatus.NOT_ACCEPTABLE.value())
                        .message(ex.getMessage())
                        .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
