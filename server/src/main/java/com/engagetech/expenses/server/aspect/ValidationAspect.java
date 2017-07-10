package com.engagetech.expenses.server.aspect;

import com.engagetech.expenses.server.error.HttpError;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Aspect
@Component
public class ValidationAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSource messageSource;

    @Around("execution(* com.engagetech.expenses.server.controller.*.*(..))")
    public Object validation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        BindingResult bindingResult = null;
        Locale locale = null;
        for (Object argument : proceedingJoinPoint.getArgs()) {
            // BindingResult
            if (argument instanceof BindingResult) {
                bindingResult = (BindingResult) argument;
                if (!bindingResult.hasErrors())
                    break;
            }
            // Locale
            if (argument instanceof Locale)
                locale = (Locale) argument;
            // Validation error
            if (bindingResult != null && locale != null && bindingResult.hasErrors()) {
                FieldError fieldError = bindingResult.getFieldError();
                Object[] fieldArguments = getFieldArguments(fieldError);
                String fieldMessage = messageSource.getMessage("validation.default.error", null, locale);
                try {
                    fieldMessage = messageSource.getMessage(fieldError.getCode(), fieldArguments, locale);
                } catch (NoSuchMessageException nsme) {
                    logger.error(nsme.getMessage());
                }
                HttpError httpError = new HttpError();
                httpError.setStatus(HttpStatus.BAD_REQUEST.value());
                httpError.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpError.setMessage(fieldMessage);
                return ResponseEntity.badRequest().body(httpError);
            }
        }
        return proceedingJoinPoint.proceed();
    }

    private Object[] getFieldArguments(FieldError fieldError) {
        List<Object> fieldArguments = new ArrayList<>(Arrays.asList(fieldError.getArguments()));
        // Add error field name to arguments
        fieldArguments.add(fieldError.getField());
        return fieldArguments.toArray(new Object[fieldArguments.size()]);
    }
}