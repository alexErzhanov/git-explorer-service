package com.erzhanov.gitexplorerservice.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(FeignException.NotFound e) {
        Map<String, Object> body = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "Message", "User not found"
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(FeignException.class)
//    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException e, WebRequest request) {
//        HttpStatus status = HttpStatus.valueOf(e.status());
//        Map<String, Object> body = Map.of(
//                "status", status.value(),
//                "Message", "Feign client error: " + e.getMessage()
//        );
//        return new ResponseEntity<>(body, status);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleAllUncaughtException(Exception e) {
//        Map<String, Object> body = Map.of(
//                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "Message", "Unexpected error: " + e.getMessage()
//        );
//        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
