package org.test.h2o.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.test.h2o.enam.ErrorCode;


/**
 * Маппер для преобразования ErrorCode в HTTP статусы.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Component
public class ErrorCodeMapper {

    /**
     * Маппинг кода ошибки на HTTP статус.
     *
     * @param errorCode код ошибки
     * @return соответствующий HTTP статус
     */
    public HttpStatus mapToHttpStatus(ErrorCode errorCode) {
        return errorCode == null ? HttpStatus.INTERNAL_SERVER_ERROR : errorCode.getHttpStatus();
    }
}