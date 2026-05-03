package org.test.h2o.swagger;

import io.swagger.v3.oas.models.Operation;
import org.springframework.web.method.HandlerMethod;

/**
 * Интерфейс для обработки операций Swagger.
 * Позволяет динамически обогащать документацию API.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
public interface OperationProcessor {

    /**
     * Обрабатывает операцию Swagger.
     *
     * @param operation операция для обработки
     * @param handlerMethod метод контроллера
     */
    void process(Operation operation, HandlerMethod handlerMethod);
}