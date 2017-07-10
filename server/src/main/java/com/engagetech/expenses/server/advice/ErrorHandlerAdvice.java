package com.engagetech.expenses.server.advice;

import com.engagetech.expenses.server.error.HttpError;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class ErrorHandlerAdvice {

    @Autowired
    private MessageSource messageSource;

    // Ejemplo para personalizar excepciones
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleConverterException(HttpMessageNotReadableException exception, Locale locale) {
        HttpError httpError = new HttpError();
        httpError.setStatus(HttpStatus.BAD_REQUEST.value());
        httpError.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        if (exception.getMostSpecificCause() instanceof InvalidFormatException) {
            InvalidFormatException formatException = (InvalidFormatException) exception.getCause();
            String[] fieldData = formatException.getPathReference().split("\\\"");
            String field = fieldData[fieldData.length - 2];
            String fieldType = formatException.getTargetType().getSimpleName();
            String fieldValue = formatException.getValue().toString();
            String message = messageSource
                    .getMessage("validation.converter.error", new String[]{fieldType, fieldValue, field}, locale);
            httpError.setMessage(message.toString());
        } else {
            httpError.setMessage(exception.getMessage());
        }
        return ResponseEntity.badRequest().body(httpError);
    }
}