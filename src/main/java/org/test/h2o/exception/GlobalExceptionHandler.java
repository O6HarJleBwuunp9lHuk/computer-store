package org.test.h2o.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.test.h2o.mapper.ErrorCodeMapper;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST API.
 * Форматирует ошибки в соответствии с RFC 7807.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ErrorCodeMapper errorCodeMapper;

    /**
     * Обрабатывает кастомные BaseException.
     */
    @ExceptionHandler(BaseException.class)
    public ProblemDetail handleBaseException(BaseException ex) {
        log.warn("Business exception: code={}, message={}", ex.getErrorCode(), ex.getMessage());

        HttpStatus status = errorCodeMapper.mapToHttpStatus(ex.getErrorCode());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());

        problemDetail.setTitle(ex.getErrorCode().name());
        problemDetail.setProperty("errorCode", ex.getErrorCode().name());
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        return problemDetail;
    }

    /**
     * Обрабатывает ошибки валидации @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof org.springframework.validation.FieldError fe
                    ? fe.getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed"
        );
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("errorCode", "VALIDATION_FAILED");
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        return problemDetail;
    }

    /**
     * Обрабатывает все остальные непредвиденные исключения.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("errorCode", "INTERNAL_SERVER_ERROR");
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        return problemDetail;
    }
}