package org.test.h2o.swagger;

import io.swagger.v3.oas.models.Operation;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Optional;

/**
 * Добавляет детальное описание к каждой операции.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Component
public class DescriptionEnhancerProcessor implements OperationProcessor {

    @Override
    public void process(Operation operation, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBeanType().getSimpleName();

        String enhancedDescription = String.format(
                "**Метод:** `%s.%s`\n\n" +
                        "**Описание:** %s\n\n" +
                        "**Валидация:** Все входные данные проходят проверку через ValidationService\n" +
                        "**Кэширование:** GET запросы кэшируются (Caffeine)",
                className,
                methodName,
                Optional.ofNullable(operation.getDescription()).orElse("Нет описания")
        );

        operation.setDescription(enhancedDescription);
    }
}