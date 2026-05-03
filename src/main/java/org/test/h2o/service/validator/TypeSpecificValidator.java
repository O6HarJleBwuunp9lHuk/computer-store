package org.test.h2o.service.validator;


import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ProductType;

/**
 * Интерфейс для валидации специфичных полей в зависимости от типа продукта.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
public interface TypeSpecificValidator {

    /**
     * Валидирует специфичные для типа поля.
     *
     * @param dto DTO для проверки
     * @throws IllegalArgumentException если валидация не пройдена
     */
    void validate(ProductRequestDto dto);

    /**
     * Возвращает тип продукта, который обрабатывает этот валидатор.
     *
     * @return тип продукта
     */
    ProductType getType();
}