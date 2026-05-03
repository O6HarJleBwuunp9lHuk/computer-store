package org.test.h2o.exception;

import org.test.h2o.enam.ErrorCode;
import lombok.Getter;

/**
 * Базовое исключение для всех бизнес-ошибок приложения.
 * Содержит ErrorCode для маппинга на HTTP статус.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}