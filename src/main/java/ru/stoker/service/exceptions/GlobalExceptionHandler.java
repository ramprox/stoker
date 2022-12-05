package ru.stoker.service.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private static final String MISSING_PARAMETER = "missing.parameter";

    private static final String ACCESS_DENIED = "access.denied";

    private static final String HTTP_MESSAGE_NOT_READABLE = "http.message.not.readable";

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        return result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleEnumConversationException(HttpMessageNotReadableException ex, Locale locale) {
        Throwable cause = ex.getCause();
        String message;
        if(cause instanceof InvalidFormatException formatException) {
            message = messageSource.getMessage(HTTP_MESSAGE_NOT_READABLE, new Object[]{ formatException.getValue() }, locale);
        } else {
            message = String.format("Ошибка: %s", ex.getMessage());
        }
        return message;
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(Locale locale) {
        return messageSource.getMessage(ACCESS_DENIED, null, locale);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleException(MissingServletRequestParameterException ex, Locale locale) {
        return messageSource.getMessage(MISSING_PARAMETER, new Object[]{ ex.getParameterName() }, locale);
    }

}
