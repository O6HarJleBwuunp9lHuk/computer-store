package org.test.h2o.swagger;

import io.swagger.v3.oas.models.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

/**
 * Собирает все процессоры и применяет их к операциям.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Component
public class OperationHandler {

    private final List<OperationProcessor> processors;

    @Autowired
    public OperationHandler(List<OperationProcessor> processors) {
        this.processors = processors;
    }

    /**
     * Обрабатывает операцию всеми зарегистрированными процессорами.
     *
     * @param operation операция Swagger
     * @param handlerMethod метод контроллера
     */
    public void handle(Operation operation, HandlerMethod handlerMethod) {
        processors.forEach(processor -> processor.process(operation, handlerMethod));
    }
}