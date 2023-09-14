package com.amigoscode.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ControllerAdvice
public class DefaultExceptionHandler {
    private final Logger LOGGER = Logger.getLogger(DefaultExceptionHandler.class.getName());
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ResourceNotFoundException e, HttpServletRequest request, HttpServletResponse response){

           ApiError apiError = new ApiError(
                    request.getRequestURI(),
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    LocalDateTime.now()
            );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleException(UsernameNotFoundException e, HttpServletRequest request, HttpServletResponse response){

        LOGGER.info("hit this exception handler");

        ApiError apiError = new ApiError(
            request.getRequestURI(),
            e.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now()
        );


        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ApiError> handleException(RequestValidationException e, HttpServletRequest request, HttpServletResponse response){
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleException(DuplicateResourceException e, HttpServletRequest request, HttpServletResponse response){
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
