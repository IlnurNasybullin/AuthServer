package org.freestudy.authserver.controller;

import org.freestudy.authserver.dto.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, List<String>> errors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> Map.entry(
                        ((FieldError) error).getField(),
                        Objects.requireNonNull(error.getDefaultMessage())
                )).collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(errors)
                .build();

        return handleExceptionInternal(exception, errorMessage, headers, status, request);
    }



}
