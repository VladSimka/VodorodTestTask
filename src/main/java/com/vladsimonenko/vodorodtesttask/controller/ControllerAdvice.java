package com.vladsimonenko.vodorodtesttask.controller;

import com.vladsimonenko.vodorodtesttask.exception.ExceptionBody;
import com.vladsimonenko.vodorodtesttask.exception.InvalidDateFormatException;
import com.vladsimonenko.vodorodtesttask.exception.InvalidParameterException;
import com.vladsimonenko.vodorodtesttask.exception.RatesAlreadyExistsException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final MessageSource messageSource;


    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<ExceptionBody> handleInvalidDateFormat(InvalidDateFormatException e, Locale locale) {
        var eb = new ExceptionBody();
        eb.setMessage(messageSource.getMessage(e.getMessage(), new Object[0], locale));

        return ResponseEntity.badRequest().body(eb);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ExceptionBody> handleInvalidParameter(InvalidParameterException e, Locale locale) {
        var eb = new ExceptionBody();
        eb.setMessage(messageSource.getMessage(e.getMessage(), new Object[0], locale));

        return ResponseEntity.badRequest().body(eb);
    }

    @ExceptionHandler(RatesAlreadyExistsException.class)
    public ResponseEntity<ExceptionBody> handleRatesAlreadyExists(RatesAlreadyExistsException e, Locale locale) {
        var eb = new ExceptionBody();
        eb.setMessage(messageSource.getMessage(e.getMessage(), new Object[0], locale));

        return ResponseEntity.badRequest().body(eb);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExceptionBody> handleFeignException(FeignException e, Locale locale) {
        int status = e.status();
        String message =
                switch (status) {
                    case 400 -> "errors.feign.bad_request";
                    case 404 -> "errors.feign.not_found";
                    case 500 -> "errors.feign.internal_error";
                    default -> "errors.feign.other";
                };
        var eb = new ExceptionBody();
        eb.setMessage(messageSource.getMessage(message, new Object[0], locale));

        return ResponseEntity.badRequest().body(eb);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionBody> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                                      Locale locale) {
        var eb = new ExceptionBody();
        eb.setMessage(messageSource.getMessage("errors.not_readable", new Object[0], locale));

        return ResponseEntity.badRequest().body(eb);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleException(Exception e, Locale locale) {
        var eb = new ExceptionBody();
        eb.setMessage(messageSource.getMessage("errors.other", new Object[0], locale));

        return ResponseEntity.badRequest().body(eb);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                      Locale locale) {
        ExceptionBody failed = new ExceptionBody();

        failed.setMessage(messageSource.getMessage("errors.validation", new Object[0], locale));
        List<FieldError> errorList = exception.getBindingResult().getFieldErrors();
        failed.setErrors(errorList.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));

        return ResponseEntity.badRequest().body(failed);
    }
}
