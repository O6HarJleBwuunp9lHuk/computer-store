package org.test.h2o.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

/**
 * DTO для ответа API с данными продукта.
 * Неизменяемый record, автоматически включает все поля в equals/hashCode.
 *
 * @param id уникальный идентификатор
 * @param serialNumber серийный номер
 * @param type тип продукта
 * @param manufacturer производитель
 * @param price цена
 * @param stockQuantity количество на складе
 * @param formFactor форм-фактор (для DESKTOP)
 * @param screenSize размер экрана (для LAPTOP)
 * @param diagonal диагональ (для MONITOR)
 * @param volumeGb объём (для HDD)
 * @param createdAt время создания (Unix timestamp)
 * @param updatedAt время обновления (Unix timestamp)
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponseDto(

        String id,
        String serialNumber,
        String type,
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

    /**
     * Builder-паттерн для удобного создания record.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Вспомогательный класс для построения ProductResponseDto.
     */
    public static final class Builder {
        private String id;
        private String serialNumber;
        private String type;
        private String manufacturer;
        private BigDecimal price;
        private Integer stockQuantity;
        private String formFactor;
        private Integer screenSize;
        private Integer diagonal;
        private Integer volumeGb;
        private Long createdAt;
        private Long updatedAt;

        private Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder manufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder stockQuantity(Integer stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public Builder formFactor(String formFactor) {
            this.formFactor = formFactor;
            return this;
        }

        public Builder screenSize(Integer screenSize) {
            this.screenSize = screenSize;
            return this;
        }

        public Builder diagonal(Integer diagonal) {
            this.diagonal = diagonal;
            return this;
        }

        public Builder volumeGb(Integer volumeGb) {
            this.volumeGb = volumeGb;
            return this;
        }

        public Builder createdAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ProductResponseDto build() {
            return new ProductResponseDto(
                    id,
                    serialNumber,
                    type,
                    manufacturer,
                    price,
                    stockQuantity,
                    formFactor,
                    screenSize,
                    diagonal,
                    volumeGb,
                    createdAt,
                    updatedAt
            );
        }
    }
}