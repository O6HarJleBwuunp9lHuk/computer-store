package org.test.h2o.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.test.h2o.config.ValidationProperties;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ProductType;
import org.test.h2o.exception.ValidationException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ValidationService Unit Tests")
class ValidationServiceTest {

    private ValidationService validationService;
    private ValidationProperties props;

    @BeforeEach
    void setUp() {
        props = new ValidationProperties();

        ValidationProperties.Serial serial = new ValidationProperties.Serial();
        serial.setMinLength(5);
        serial.setMaxLength(50);
        serial.setPattern("^[A-Z0-9-]+$");
        props.setSerial(serial);

        ValidationProperties.Manufacturer manufacturer = new ValidationProperties.Manufacturer();
        manufacturer.setMinLength(2);
        manufacturer.setMaxLength(100);
        props.setManufacturer(manufacturer);

        ValidationProperties.Price price = new ValidationProperties.Price();
        price.setMin("0.01");
        price.setMax("9999999.99");
        props.setPrice(price);

        ValidationProperties.Stock stock = new ValidationProperties.Stock();
        stock.setMin(0);
        stock.setMax(100000);
        props.setStock(stock);

        ValidationProperties.Desktop desktop = new ValidationProperties.Desktop();
        desktop.setFormFactors(List.of("DESKTOP", "NETTOP", "ALL_IN_ONE"));
        props.setDesktop(desktop);

        ValidationProperties.Laptop laptop = new ValidationProperties.Laptop();
        laptop.setScreenSizes(List.of(13, 14, 15, 17));
        props.setLaptop(laptop);

        ValidationProperties.ScreenSize screenSize = new ValidationProperties.ScreenSize();
        screenSize.setMin(13);
        screenSize.setMax(17);
        props.setScreenSize(screenSize);

        ValidationProperties.Diagonal diagonal = new ValidationProperties.Diagonal();
        diagonal.setMin(10);
        diagonal.setMax(49);
        props.setDiagonal(diagonal);

        ValidationProperties.VolumeGb volumeGb = new ValidationProperties.VolumeGb();
        volumeGb.setMin(120);
        volumeGb.setMax(20000);
        props.setVolumeGb(volumeGb);

        validationService = new ValidationService(props, List.of());
    }

    @Nested
    @DisplayName("validateSerialNumber")
    class ValidateSerialNumber {

        @Test
        @DisplayName("throws ValidationException when serial number is null")
        void throwsException_whenSerialNumberIsNull() {
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, null, "Apple",
                    new BigDecimal("1000"), 10,
                    null, 15, null, null
            );

            assertThatThrownBy(() -> validationService.validate(dto))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Serial number is required");
        }

        @Test
        @DisplayName("throws ValidationException when serial number is blank")
        void throwsException_whenSerialNumberIsBlank() {
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, "", "Apple",
                    new BigDecimal("1000"), 10,
                    null, 15, null, null
            );

            assertThatThrownBy(() -> validationService.validate(dto))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Serial number is required");
        }
    }

    @Nested
    @DisplayName("validateManufacturer")
    class ValidateManufacturer {

        @Test
        @DisplayName("throws ValidationException when manufacturer is null")
        void throwsException_whenManufacturerIsNull() {
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, "SN123", null,
                    new BigDecimal("1000"), 10,
                    null, 15, null, null
            );

            assertThatThrownBy(() -> validationService.validate(dto))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Manufacturer is required");
        }
    }

    @Nested
    @DisplayName("validatePrice")
    class ValidatePrice {

        @Test
        @DisplayName("throws ValidationException when price is null")
        void throwsException_whenPriceIsNull() {
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, "SN123", "Apple",
                    null, 10,
                    null, 15, null, null
            );

            assertThatThrownBy(() -> validationService.validate(dto))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Price is required");
        }
    }

    @Nested
    @DisplayName("validateProductTypeUnchanged")
    class ValidateProductTypeUnchanged {

        @Test
        @DisplayName("throws ValidationException when type changes")
        void throwsException_whenTypeChanges() {
            assertThatThrownBy(() -> validationService.validateProductTypeUnchanged(ProductType.LAPTOP, ProductType.DESKTOP))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot change product type");
        }

        @Test
        @DisplayName("does not throw when type does not change")
        void doesNotThrow_whenTypeDoesNotChange() {
            validationService.validateProductTypeUnchanged(ProductType.LAPTOP, ProductType.LAPTOP);
        }
    }
}