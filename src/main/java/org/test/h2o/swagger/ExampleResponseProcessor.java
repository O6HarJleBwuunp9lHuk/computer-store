package org.test.h2o.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * Добавляет примеры ответов для каждой операции.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Component
public class ExampleResponseProcessor implements OperationProcessor {

    @Override
    public void process(Operation operation, HandlerMethod handlerMethod) {
        ApiResponses responses = operation.getResponses();
        if (responses == null) {
            responses = new ApiResponses();
            operation.setResponses(responses);
        }

        // Пример успешного ответа (200)
        if (!responses.containsKey("200")) {
            ApiResponse successResponse = new ApiResponse()
                    .description("Успешный ответ")
                    .content(new Content()
                            .addMediaType("application/json", new MediaType()
                                    .example("""
                            {
                                "id": "65f7a1b2c3d4e5f6g7h8i9j0",
                                "serialNumber": "SN-TEST-001",
                                "type": "LAPTOP",
                                "manufacturer": "Apple",
                                "price": 129999,
                                "stockQuantity": 10,
                                "screenSize": 15,
                                "createdAt": 1698768000000,
                                "updatedAt": 1698768000000
                            }
                            """)
                            )
                    );
            responses.addApiResponse("200", successResponse);
        }

        // Пример ошибки валидации (400)
        if (!responses.containsKey("400")) {
            ApiResponse errorResponse = new ApiResponse()
                    .description("Ошибка валидации")
                    .content(new Content()
                            .addMediaType("application/json", new MediaType()
                                    .example("""
                            {
                                "status": 400,
                                "title": "Validation Error",
                                "detail": "Serial number is required",
                                "errorCode": "VALIDATION_REQUIRED_FIELD_MISSING",
                                "fieldName": "serialNumber",
                                "timestamp": 1698768000000
                            }
                            """)
                            )
                    );
            responses.addApiResponse("400", errorResponse);
        }

        // Пример ошибки 404 (не найдено)
        if (!responses.containsKey("404")) {
            ApiResponse notFoundResponse = new ApiResponse()
                    .description("Товар не найден")
                    .content(new Content()
                            .addMediaType("application/json", new MediaType()
                                    .example("""
                            {
                                "status": 404,
                                "title": "Product Not Found",
                                "detail": "Product with id '123' not found",
                                "errorCode": "PRODUCT_NOT_FOUND",
                                "timestamp": 1698768000000
                            }
                            """)
                            )
                    );
            responses.addApiResponse("404", notFoundResponse);
        }

        // Пример ошибки 409 (конфликт)
        if (!responses.containsKey("409")) {
            ApiResponse conflictResponse = new ApiResponse()
                    .description("Конфликт (дубликат серийного номера)")
                    .content(new Content()
                            .addMediaType("application/json", new MediaType()
                                    .example("""
                            {
                                "status": 409,
                                "title": "Duplicate Serial Number",
                                "detail": "Product with serial number 'SN-TEST-001' already exists",
                                "errorCode": "DUPLICATE_SERIAL_NUMBER",
                                "timestamp": 1698768000000
                            }
                            """)
                            )
                    );
            responses.addApiResponse("409", conflictResponse);
        }
    }
}