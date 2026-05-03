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

@Configuration
public class OpenApiConfig {

    public static final String BEARER_SCHEME = "Bearer";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Computer Store API")
                        .version("1.0")
                        .description("REST API для управления каталогом компьютерной техники")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com"))
                        .license(new License()
                                .name("MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8085").description("Local server")
                ))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, createBearerSecurityScheme())
                );
    }

    @Bean
    public OperationCustomizer customizeOperation(OperationHandler operationHandler) {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operationHandler.handle(operation, handlerMethod);
            return operation;
        };
    }

    private SecurityScheme createBearerSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT токен для авторизации");
    }
}