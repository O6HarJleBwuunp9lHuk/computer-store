package org.test.h2o.exception;

import lombok.Getter;
import org.test.h2o.enam.ErrorCode;

/**
 * Исключение для ошибок валидации входных данных.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Getter
public class ValidationException extends BaseException {

    private final String fieldName;
    private final Object rejectedValue;

    public ValidationException(String message, ErrorCode errorCode, String fieldName, Object rejectedValue) {
        super(message, errorCode);
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
    }
}