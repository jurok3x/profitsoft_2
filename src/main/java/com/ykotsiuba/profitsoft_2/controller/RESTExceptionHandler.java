package com.ykotsiuba.profitsoft_2.controller;

import com.ykotsiuba.profitsoft_2.dto.APIException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RESTExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ EntityNotFoundException.class })
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        APIException apiException = new APIException("Entity not found", HttpStatus.NOT_FOUND,
                LocalDateTime.now(), details);
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        APIException apiException = new APIException("Invalid argument", HttpStatus.BAD_REQUEST,
                LocalDateTime.now(), details);
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        APIException apiException = new APIException("Invalid argument", HttpStatus.BAD_REQUEST,
                LocalDateTime.now(), details);

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
