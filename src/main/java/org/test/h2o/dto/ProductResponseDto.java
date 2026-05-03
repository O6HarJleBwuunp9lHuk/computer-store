package org.test.h2o.dto;

import lombok.Builder;
import org.test.h2o.enam.ProductType;

import java.math.BigDecimal;

/**
 * DTO для ответа API с данными продукта.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */

@Builder
public record ProductResponseDto(

        String id,
        String serialNumber,
        ProductType type,
        String manufacturer,
        BigDecimal price,
        Integer stockQuantity,
        String formFactor,
        Integer screenSize,
        Integer diagonal,
        Integer volumeGb,
        Long createdAt,
        Long updatedAt
) {
}