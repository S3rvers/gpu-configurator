package com.amalitech.gpuconfigurator.exception;

import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({MessagingException.class})
    public ProblemDetail handleEmailSendingException(MessagingException e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(500));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }


    @ExceptionHandler({MethodArgumentNotValidException.class, DataIntegrityViolationException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
        errorDetail.setDetail("One or more validation errors occurred");

        Map<String, Object> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        errorDetail.setProperty("field_errors", fieldErrors);
        return errorDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialException(Exception e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(401));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail usernameNotFoundException(Exception e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(404));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }

}
