package org.test.h2o.dto;

import org.test.h2o.enam.ProductType;

import java.math.BigDecimal;

/**
 * DTO для создания и обновления продукта.
 * Использует record для неизменяемости.
 *
 * <p>Валидация происходит в {@link org.test.h2o.service.ValidationService}
 * с использованием конфигурации из application.yml.
 *
 * @param type          тип продукта (DESKTOP, LAPTOP, MONITOR, HDD)
 * @param serialNumber  уникальный серийный номер
 * @param manufacturer  производитель
 * @param price         цена в рублях
 * @param stockQuantity количество на складе
 * @param formFactor    форм-фактор (для DESKTOP)
 * @param screenSize    размер экрана (для LAPTOP)
 * @param diagonal      диагональ (для MONITOR)
 * @param volumeGb      объём в ГБ (для HDD)
 * @author Bredikhin Andrey
 * @version 1.0
 */
public record ProductRequestDto(

        ProductType type,
        String serialNumber,
        String manufacturer,
        BigDecimal price,
        Integer stockQuantity,
        String formFactor,
        Integer screenSize,
        Integer diagonal,
        Integer volumeGb
) {
}