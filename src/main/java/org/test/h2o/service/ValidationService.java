package org.test.h2o.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.test.h2o.config.ValidationProperties;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ErrorCode;
import org.test.h2o.enam.ProductType;

import org.test.h2o.exception.ValidationException;
import org.test.h2o.service.validator.TypeSpecificValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Сервис для валидации DTO с использованием конфигурации.
 * Использует стратегию для валидации специфичных полей.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Service
@Slf4j
public class ValidationService {

    private final ValidationProperties props;
    private final Map<ProductType, TypeSpecificValidator> validators;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final Pattern serialPattern;

    public ValidationService(
            ValidationProperties props,
            List<TypeSpecificValidator> validatorList
    ) {
        this.props = props;
        this.validators = new ConcurrentHashMap<>();

        for (TypeSpecificValidator validator : validatorList) {
            validators.put(validator.getType(), validator);
            log.debug("Registered validator for type: {}", validator.getType());
        }

        this.minPrice = new BigDecimal(props.getPrice().getMin());
        this.maxPrice = new BigDecimal(props.getPrice().getMax());
        this.serialPattern = Pattern.compile(props.getSerial().getPattern());
    }

    /**
     * Полная валидация DTO.
     */
    public void validate(ProductRequestDto dto) {
        log.debug("Validating product: type={}, serial={}", dto.type(), dto.serialNumber());

        validateRequired(dto.serialNumber(), "Serial number", ErrorCode.VALIDATION_REQUIRED_FIELD_MISSING);
        validateRequired(dto.manufacturer(), "Manufacturer", ErrorCode.VALIDATION_REQUIRED_FIELD_MISSING);
        validateRequired(dto.price(), "Price", ErrorCode.VALIDATION_REQUIRED_FIELD_MISSING);
        validateRequired(dto.stockQuantity(), "Stock quantity", ErrorCode.VALIDATION_REQUIRED_FIELD_MISSING);

        validateSerialNumber(dto.serialNumber());
        validateManufacturer(dto.manufacturer());
        validatePrice(dto.price());
        validateStockQuantity(dto.stockQuantity());
        validateSpecificFields(dto);

        log.debug("Validation passed for product: serial={}", dto.serialNumber());
    }

    /**
     * Проверяет, что тип продукта не меняется при обновлении.
     */
    public void validateProductTypeUnchanged(ProductType existingType, ProductType newType) {
        if (!existingType.equals(newType)) {
            throw new ValidationException(
                    String.format("Cannot change product type from '%s' to '%s'", existingType, newType),
                    ErrorCode.CANNOT_CHANGE_PRODUCT_TYPE,
                    "type",
                    newType
            );
        }
    }

    /**
     * Валидирует и нормализует тип продукта для поиска.
     */

    private void validateRequired(Object value, String fieldName, ErrorCode errorCode) {
        if (value == null) {
            throw new ValidationException(
                    String.format("%s is required", fieldName),
                    errorCode,
                    fieldName,
                    null
            );
        }
    }

    private void validateNotBlank(String value, String fieldName, ErrorCode errorCode) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(
                    String.format("%s is required", fieldName),
                    errorCode,
                    fieldName,
                    value
            );
        }
    }

    private void validateSerialNumber(String serialNumber) {
        validateNotBlank(serialNumber, "Serial number", ErrorCode.VALIDATION_REQUIRED_FIELD_MISSING);

        int min = props.getSerial().getMinLength();
        int max = props.getSerial().getMaxLength();

        if (serialNumber.length() < min || serialNumber.length() > max) {
            throw new ValidationException(
                    String.format("Serial number must be between %d and %d characters", min, max),
                    ErrorCode.VALIDATION_SERIAL_LENGTH_INVALID,
                    "serialNumber",
                    serialNumber
            );
        }

        if (!serialPattern.matcher(serialNumber).matches()) {
            throw new ValidationException(
                    "Serial number must contain only uppercase letters, numbers and hyphens",
                    ErrorCode.VALIDATION_SERIAL_INVALID_FORMAT,
                    "serialNumber",
                    serialNumber
            );
        }
    }

    private void validateManufacturer(String manufacturer) {
        validateNotBlank(manufacturer, "Manufacturer", ErrorCode.VALIDATION_REQUIRED_FIELD_MISSING);

        int min = props.getManufacturer().getMinLength();
        int max = props.getManufacturer().getMaxLength();

        if (manufacturer.length() < min || manufacturer.length() > max) {
            throw new ValidationException(
                    String.format("Manufacturer name must be between %d and %d characters", min, max),
                    ErrorCode.VALIDATION_MANUFACTURER_LENGTH_INVALID,
                    "manufacturer",
                    manufacturer
            );
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(minPrice) < 0 || price.compareTo(maxPrice) > 0) {
            throw new ValidationException(
                    String.format("Price must be between %s and %s", minPrice, maxPrice),
                    ErrorCode.VALIDATION_PRICE_OUT_OF_RANGE,
                    "price",
                    price
            );
        }
    }

    private void validateStockQuantity(Integer quantity) {
        int min = props.getStock().getMin();
        int max = props.getStock().getMax();

        if (quantity < min || quantity > max) {
            throw new ValidationException(
                    String.format("Stock quantity must be between %d and %d", min, max),
                    ErrorCode.VALIDATION_STOCK_OUT_OF_RANGE,
                    "stockQuantity",
                    quantity
            );
        }
    }

    private void validateSpecificFields(ProductRequestDto dto) {
        TypeSpecificValidator validator = validators.get(dto.type());

        if (validator == null) {
            throw new ValidationException(
                    String.format("No validator found for product type: %s", dto.type()),
                    ErrorCode.VALIDATION_TYPE_SPECIFIC_FIELDS_INVALID,
                    "type",
                    dto.type()
            );
        }

        validator.validate(dto);
    }
}