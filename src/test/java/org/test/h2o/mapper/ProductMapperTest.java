package org.test.h2o.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.dto.ProductResponseDto;
import org.test.h2o.enam.ProductType;
import org.test.h2o.model.Product;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductMapper Unit Tests")
class ProductMapperTest {

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        @DisplayName("маппит LAPTOP DTO в Entity со всеми полями")
        void mapsLaptopDtoToEntity() {
            // Given
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, "SN-LAPTOP-001", "Apple",
                    new BigDecimal("129999"), 10,
                    null, 15, null, null
            );

            // When
            Product entity = productMapper.toEntity(dto);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getSerialNumber()).isEqualTo("SN-LAPTOP-001");
            assertThat(entity.getType()).isEqualTo(ProductType.LAPTOP);
            assertThat(entity.getManufacturer()).isEqualTo("Apple");
            assertThat(entity.getPrice()).isEqualTo(new BigDecimal("129999"));
            assertThat(entity.getStockQuantity()).isEqualTo(10);
            assertThat(entity.getScreenSize()).isEqualTo(15);
            assertThat(entity.getFormFactor()).isNull();
            assertThat(entity.getDiagonal()).isNull();
            assertThat(entity.getVolumeGb()).isNull();
        }

        @Test
        @DisplayName("маппит DESKTOP DTO в Entity со всеми полями")
        void mapsDesktopDtoToEntity() {
            // Given
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.DESKTOP, "SN-DESKTOP-001", "Dell",
                    new BigDecimal("89999"), 5,
                    "ALL_IN_ONE", null, null, null
            );

            // When
            Product entity = productMapper.toEntity(dto);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getSerialNumber()).isEqualTo("SN-DESKTOP-001");
            assertThat(entity.getType()).isEqualTo(ProductType.DESKTOP);
            assertThat(entity.getManufacturer()).isEqualTo("Dell");
            assertThat(entity.getPrice()).isEqualTo(new BigDecimal("89999"));
            assertThat(entity.getStockQuantity()).isEqualTo(5);
            assertThat(entity.getFormFactor()).isEqualTo("ALL_IN_ONE");
            assertThat(entity.getScreenSize()).isNull();
            assertThat(entity.getDiagonal()).isNull();
            assertThat(entity.getVolumeGb()).isNull();
        }

        @Test
        @DisplayName("маппит MONITOR DTO в Entity")
        void mapsMonitorDtoToEntity() {
            // Given
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.MONITOR, "SN-MONITOR-001", "Samsung",
                    new BigDecimal("45999"), 8,
                    null, null, 27, null
            );

            // When
            Product entity = productMapper.toEntity(dto);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getSerialNumber()).isEqualTo("SN-MONITOR-001");
            assertThat(entity.getType()).isEqualTo(ProductType.MONITOR);
            assertThat(entity.getManufacturer()).isEqualTo("Samsung");
            assertThat(entity.getPrice()).isEqualTo(new BigDecimal("45999"));
            assertThat(entity.getStockQuantity()).isEqualTo(8);
            assertThat(entity.getDiagonal()).isEqualTo(27);
        }

        @Test
        @DisplayName("маппит HDD DTO в Entity")
        void mapsHddDtoToEntity() {
            // Given
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.HDD, "SN-HDD-001", "Western Digital",
                    new BigDecimal("12999"), 20,
                    null, null, null, 1000
            );

            // When
            Product entity = productMapper.toEntity(dto);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getSerialNumber()).isEqualTo("SN-HDD-001");
            assertThat(entity.getType()).isEqualTo(ProductType.HDD);
            assertThat(entity.getManufacturer()).isEqualTo("Western Digital");
            assertThat(entity.getPrice()).isEqualTo(new BigDecimal("12999"));
            assertThat(entity.getStockQuantity()).isEqualTo(20);
            assertThat(entity.getVolumeGb()).isEqualTo(1000);
        }

        @Test
        @DisplayName("маппит null поля как null")
        void mapsNullFieldsAsNull() {
            // Given
            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, "SN-NULL-001", null,
                    null, null,
                    null, null, null, null
            );

            // When
            Product entity = productMapper.toEntity(dto);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getSerialNumber()).isEqualTo("SN-NULL-001");
            assertThat(entity.getManufacturer()).isNull();
            assertThat(entity.getPrice()).isNull();
            assertThat(entity.getStockQuantity()).isNull();
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponse {

        @Test
        @DisplayName("маппит LAPTOP Entity в Response DTO")
        void mapsLaptopEntityToResponse() {
            // Given
            Product product = new Product();
            product.setId("65f7a1b2c3d4e5f6g7h8i9j0");
            product.setSerialNumber("SN-LAPTOP-001");
            product.setType(ProductType.LAPTOP);
            product.setManufacturer("Apple");
            product.setPrice(new BigDecimal("129999"));
            product.setStockQuantity(10);
            product.setScreenSize(15);
            product.setCreatedAt(1698768000000L);
            product.setUpdatedAt(1698768000000L);

            // When
            ProductResponseDto response = productMapper.toResponse(product);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo("65f7a1b2c3d4e5f6g7h8i9j0");
            assertThat(response.serialNumber()).isEqualTo("SN-LAPTOP-001");
            assertThat(response.type()).isEqualTo(ProductType.LAPTOP);
            assertThat(response.manufacturer()).isEqualTo("Apple");
            assertThat(response.price()).isEqualTo(new BigDecimal("129999"));
            assertThat(response.stockQuantity()).isEqualTo(10);
            assertThat(response.screenSize()).isEqualTo(15);
            assertThat(response.createdAt()).isEqualTo(1698768000000L);
            assertThat(response.updatedAt()).isEqualTo(1698768000000L);
        }

        @Test
        @DisplayName("маппит DESKTOP Entity в Response DTO")
        void mapsDesktopEntityToResponse() {
            // Given
            Product product = new Product();
            product.setId("desktop-id-123");
            product.setSerialNumber("SN-DESKTOP-001");
            product.setType(ProductType.DESKTOP);
            product.setManufacturer("Dell");
            product.setPrice(new BigDecimal("89999"));
            product.setStockQuantity(5);
            product.setFormFactor("ALL_IN_ONE");
            product.setCreatedAt(1698768000000L);
            product.setUpdatedAt(1698768000000L);

            // When
            ProductResponseDto response = productMapper.toResponse(product);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo("desktop-id-123");
            assertThat(response.serialNumber()).isEqualTo("SN-DESKTOP-001");
            assertThat(response.type()).isEqualTo(ProductType.DESKTOP);
            assertThat(response.formFactor()).isEqualTo("ALL_IN_ONE");
            assertThat(response.screenSize()).isNull();
        }

        @Test
        @DisplayName("маппит MONITOR Entity в Response DTO")
        void mapsMonitorEntityToResponse() {
            // Given
            Product product = new Product();
            product.setId("monitor-id-123");
            product.setSerialNumber("SN-MONITOR-001");
            product.setType(ProductType.MONITOR);
            product.setManufacturer("Samsung");
            product.setPrice(new BigDecimal("45999"));
            product.setStockQuantity(8);
            product.setDiagonal(27);

            // When
            ProductResponseDto response = productMapper.toResponse(product);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.diagonal()).isEqualTo(27);
        }

        @Test
        @DisplayName("маппит HDD Entity в Response DTO")
        void mapsHddEntityToResponse() {
            // Given
            Product product = new Product();
            product.setId("hdd-id-123");
            product.setSerialNumber("SN-HDD-001");
            product.setType(ProductType.HDD);
            product.setManufacturer("Western Digital");
            product.setPrice(new BigDecimal("12999"));
            product.setStockQuantity(20);
            product.setVolumeGb(1000);

            // When
            ProductResponseDto response = productMapper.toResponse(product);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.volumeGb()).isEqualTo(1000);
        }
    }

    @Nested
    @DisplayName("updateEntity")
    class UpdateEntity {

        @Test
        @DisplayName("обновляет только не-null поля из DTO")
        void updatesOnlyNonNullFields() {
            // Given
            Product product = new Product();
            product.setSerialNumber("OLD-SN");
            product.setManufacturer("Old Manufacturer");
            product.setPrice(new BigDecimal("100"));
            product.setStockQuantity(1);
            product.setScreenSize(13);

            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, "NEW-SN", "New Manufacturer",
                    new BigDecimal("200"), null,
                    null, null, null, null
            );

            // When
            productMapper.updateEntity(product, dto);

            // Then
            assertThat(product.getSerialNumber()).isEqualTo("NEW-SN");
            assertThat(product.getManufacturer()).isEqualTo("New Manufacturer");
            assertThat(product.getPrice()).isEqualTo(new BigDecimal("200"));
            assertThat(product.getStockQuantity()).isEqualTo(1); // не изменилось
            assertThat(product.getScreenSize()).isEqualTo(13);   // не изменилось
        }

        @Test
        @DisplayName("не обновляет Entity, когда DTO содержит только null")
        void doesNotUpdateWhenDtoHasOnlyNulls() {
            // Given
            Product product = new Product();
            product.setSerialNumber("ORIGINAL-SN");
            product.setManufacturer("Original");
            product.setPrice(new BigDecimal("100"));
            product.setStockQuantity(10);

            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, null, null,
                    null, null,
                    null, null, null, null
            );

            // When
            productMapper.updateEntity(product, dto);

            // Then
            assertThat(product.getSerialNumber()).isEqualTo("ORIGINAL-SN");
            assertThat(product.getManufacturer()).isEqualTo("Original");
            assertThat(product.getPrice()).isEqualTo(new BigDecimal("100"));
            assertThat(product.getStockQuantity()).isEqualTo(10);
        }

        @Test
        @DisplayName("обновляет специфичные поля для DESKTOP")
        void updatesDesktopSpecificFields() {
            // Given
            Product product = new Product();
            product.setFormFactor("DESKTOP");

            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.DESKTOP, null, null,
                    null, null,
                    "ALL_IN_ONE", null, null, null
            );

            // When
            productMapper.updateEntity(product, dto);

            // Then
            assertThat(product.getFormFactor()).isEqualTo("ALL_IN_ONE");
        }

        @Test
        @DisplayName("обновляет специфичные поля для LAPTOP")
        void updatesLaptopSpecificFields() {
            // Given
            Product product = new Product();
            product.setScreenSize(13);

            ProductRequestDto dto = new ProductRequestDto(
                    ProductType.LAPTOP, null, null,
                    null, null,
                    null, 15, null, null
            );

            // When
            productMapper.updateEntity(product, dto);

            // Then
            assertThat(product.getScreenSize()).isEqualTo(15);
        }
    }

    @Nested
    @DisplayName("Round Trip")
    class RoundTrip {

        @Test
        @DisplayName("DTO -> Entity -> Response сохраняет все данные")
        void roundTripPreservesData() {
            // Given
            ProductRequestDto originalDto = new ProductRequestDto(
                    ProductType.LAPTOP, "SN-ROUNDTRIP-001", "Apple",
                    new BigDecimal("129999"), 10,
                    null, 15, null, null
            );

            // When
            Product entity = productMapper.toEntity(originalDto);
            ProductResponseDto response = productMapper.toResponse(entity);

            // Then
            assertThat(response.serialNumber()).isEqualTo(originalDto.serialNumber());
            assertThat(response.type()).isEqualTo(originalDto.type());
            assertThat(response.manufacturer()).isEqualTo(originalDto.manufacturer());
            assertThat(response.price()).isEqualTo(originalDto.price());
            assertThat(response.stockQuantity()).isEqualTo(originalDto.stockQuantity());
            assertThat(response.screenSize()).isEqualTo(originalDto.screenSize());
        }
    }
}