package org.test.h2o.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.test.h2o.swagger.OperationHandler;
import io.swagger.v3.oas.models.Operation;

import java.util.List;

/**
 * Конфигурация OpenAPI (Swagger) для документирования REST API.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {

    public static final String BEARER_SCHEME = "Bearer";

    /**
     * Настраивает мета-информацию OpenAPI.
     *
     * @return кастомизированный OpenAPI объект
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Computer Store API")
                        .version("1.0")
                        .description("""
                                REST API для управления каталогом компьютерной техники.
                                
                                ## Возможности
                                - Добавление товаров
                                - Редактирование товаров
                                - Просмотр по типу
                                - Просмотр по ID
                                """)
                        .contact(new Contact()
                                .name("Bredikhin Andrey")
                                .email("anri23092003@gmail.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8085")
                                .description("Локальный сервер разработки"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Docker контейнер")
                ))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, createBearerSecurityScheme())
                );
    }

    /**
     * Кастомный OperationCustomizer для применения всех процессоров.
     *
     * @param operationHandler обработчик операций
     * @return кастомизированный OperationCustomizer
     */
    @Bean
    public OperationCustomizer customizeOperation(OperationHandler operationHandler) {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operationHandler.handle(operation, handlerMethod);
            return operation;
        };
    }

    /**
     * Создаёт схему безопасности Bearer Token.
     *
     * @return SecurityScheme для JWT
     */
    private SecurityScheme createBearerSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT токен для авторизации. Формат: Bearer <token>");
    }
}