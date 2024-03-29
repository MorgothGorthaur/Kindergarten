package com.example.demo.controller;

import com.example.demo.dto.ApiError;
import com.example.demo.exception.kindergarten.notacceptable.*;
import com.example.demo.exception.kindergarten.notfound.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundEx(RuntimeException ex) {
        var apiError = new ApiError("entity not found exception", List.of(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotAcceptableDataException.class})
    protected ResponseEntity<Object> handleDataNotAcceptableEx(RuntimeException ex) {
        var apiError = new ApiError("This data is not acceptable!", List.of(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidArgument(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        var apiError = new ApiError("validation error", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        var apiError = new ApiError("Malformed JSON Request", List.of(Objects.requireNonNull(ex.getMessage())));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        var apiError = new ApiError("bad argument type",
                List.of(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                        ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName())));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
