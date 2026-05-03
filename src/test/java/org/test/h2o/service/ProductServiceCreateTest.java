package org.test.h2o.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.dto.ProductResponseDto;
import org.test.h2o.enam.ProductType;
import org.test.h2o.exception.DuplicateSerialException;
import org.test.h2o.model.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;


class ProductServiceCreateTest extends ProductServiceTestBase {

    @Nested
    @DisplayName("createProduct")
    class CreateProduct {

        @Test
        @DisplayName("returnsSavedProduct, когда все данные валидны")
        void returnsSavedProduct_whenAllDataIsValid() {
            // Given
            when(productRepository.existsBySerialNumber(testSerial)).thenReturn(false);

            when(productMapper.toEntity(any(ProductRequestDto.class))).thenReturn(product);

            when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
                Product p = invocation.getArgument(0);
                return ProductResponseDto.builder()
                        .id(p.getId())
                        .serialNumber(p.getSerialNumber())
                        .type(p.getType())
                        .manufacturer(p.getManufacturer())
                        .price(p.getPrice())
                        .stockQuantity(p.getStockQuantity())
                        .screenSize(p.getScreenSize())
                        .build();
            });

            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            // When
            ProductResponseDto result = productService.createProduct(requestDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(testId);
            assertThat(result.serialNumber()).isEqualTo(testSerial);
            assertThat(result.type()).isEqualTo(ProductType.LAPTOP);

            verify(productRepository).existsBySerialNumber(testSerial);
            verify(productRepository).save(any(Product.class));
            verify(validationService).validate(requestDto);
        }

        @Test
        @DisplayName("throwsDuplicateSerialException, когда серийный номер уже существует")
        void throwsDuplicateSerialException_whenSerialNumberAlreadyExists() {
            // Given
            when(productRepository.existsBySerialNumber(testSerial)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> productService.createProduct(requestDto))
                    .isInstanceOf(DuplicateSerialException.class)
                    .hasMessageContaining(testSerial);

            verify(productRepository).existsBySerialNumber(testSerial);
            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("callsValidationService, always")
        void callsValidationService_always() {
            // Given
            when(productRepository.existsBySerialNumber(testSerial)).thenReturn(false);
            when(productMapper.toEntity(any(ProductRequestDto.class))).thenReturn(product);
            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            // When
            productService.createProduct(requestDto);

            // Then
            verify(validationService).validate(requestDto);
        }
    }
}