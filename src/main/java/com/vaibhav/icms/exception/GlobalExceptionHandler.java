package com.vaibhav.icms.exception;

import com.vaibhav.icms.exception.ex.EmailAlreadyExistsException;
import com.vaibhav.icms.exception.ex.ResourceNotFoundException;
import com.vaibhav.icms.exception.ex.UserAlreadyAssignedException;
import com.vaibhav.icms.exception.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 -Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest request){
        return buildApiErrorResponse(HttpStatus.NOT_FOUND,ex.getMessage(),request);
    }

    // 409 - Conflict (Duplicate emails)
    @ExceptionHandler({EmailAlreadyExistsException.class, UserAlreadyAssignedException.class})
    public ResponseEntity<ApiErrorResponse> handleAlreadyExists(EmailAlreadyExistsException ex,WebRequest request){
        return buildApiErrorResponse(HttpStatus.CONFLICT,ex.getMessage(),request);
    }

    // 403 - Forbidden ( RBAC or security Failures )
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request){
        return buildApiErrorResponse(HttpStatus.FORBIDDEN, "you do not have permissions to perform this action.",request);
    }

    // 400 - validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed: " + details, request);
    }

    // 500 - genereic system error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobal(Exception ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected system error occurred", request);
        }


    public ResponseEntity<ApiErrorResponse> buildApiErrorResponse(HttpStatus status,String message, WebRequest request){
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(error,status);
    }

}
