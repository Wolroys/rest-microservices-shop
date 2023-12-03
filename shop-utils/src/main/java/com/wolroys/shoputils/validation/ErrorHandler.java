package com.wolroys.shoputils.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ErrorHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e){
        final List<Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        return new ValidationErrorResponse(violations);
    }
}
