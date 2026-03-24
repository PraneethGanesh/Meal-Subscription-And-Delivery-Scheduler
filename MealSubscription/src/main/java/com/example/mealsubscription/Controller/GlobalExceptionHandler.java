package com.example.mealsubscription.Controller;

import com.example.mealsubscription.Exceptions.SubscriptionAlreadyExists;
import com.example.mealsubscription.Exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildError(HttpStatus status, String message, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(
            UserNotFoundException exception, WebRequest request) {

        return new ResponseEntity<>(
                buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(SubscriptionAlreadyExists.class)
    public ResponseEntity<Map<String, Object>> handleSubscriptionAlreadyExists(
            SubscriptionAlreadyExists exception, WebRequest request) {

        return new ResponseEntity<>(
                buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), request),
                HttpStatus.BAD_REQUEST
        );
    }

}