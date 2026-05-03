package org.test.h2o.enam;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Типы товаров в компьютерном магазине.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
public enum ProductType {

    DESKTOP("DESKTOP"),
    LAPTOP("LAPTOP"),
    MONITOR("MONITOR"),
    HDD("HDD");

    private final String value;

    ProductType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ProductType fromValue(String value) {
        for (ProductType type : ProductType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown product type: " + value);
    }
}