package org.test.h2o.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.dto.ProductResponseDto;
import org.test.h2o.enam.ProductType;
import org.test.h2o.exception.ProductNotFoundException;
import org.test.h2o.model.Product;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doThrow;


class ProductServiceUpdateTest extends ProductServiceTestBase {

    @Nested
    @DisplayName("updateProduct")
    class UpdateProduct {

        @Test
        @DisplayName("returnsUpdatedProduct, когда продукт найден")
        void returnsUpdatedProduct_whenProductFound() {
            // Given
            when(productRepository.findById(testId)).thenReturn(Optional.of(product));

            doAnswer(invocation -> {
                Product p = invocation.getArgument(0);
                ProductRequestDto d = invocation.getArgument(1);
                if (d.price() != null) p.setPrice(d.price());
                if (d.stockQuantity() != null) p.setStockQuantity(d.stockQuantity());
                return null;
            }).when(productMapper).updateEntity(any(Product.class), any(ProductRequestDto.class));

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

            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
                Product saved = invocation.getArgument(0);
                saved.setId(testId);
                return saved;
            });

            ProductRequestDto updateRequest = new ProductRequestDto(
                    ProductType.LAPTOP, testSerial, "Apple",
                    new BigDecimal("99999"), 5,
                    null, 15, null, null
            );

            // When
            ProductResponseDto result = productService.updateProduct(testId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.price()).isEqualTo(new BigDecimal("99999"));
            assertThat(result.stockQuantity()).isEqualTo(5);

            verify(productRepository).findById(testId);
            verify(productMapper).updateEntity(any(), any());
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("throwsProductNotFoundException, когда продукт не найден")
        void throwsProductNotFoundException_whenProductNotFound() {
            // Given
            when(productRepository.findById(testId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> productService.updateProduct(testId, requestDto))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessageContaining(testId);

            verify(productRepository).findById(testId);
            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("throwsIllegalArgumentException, когда тип продукта меняется")
        void throwsIllegalArgumentException_whenProductTypeChanges() {
            // Given
            when(productRepository.findById(testId)).thenReturn(Optional.of(product));
            doThrow(new IllegalArgumentException("Cannot change product type"))
                    .when(validationService).validateProductTypeUnchanged(ProductType.LAPTOP, ProductType.DESKTOP);

            ProductRequestDto differentTypeRequest = new ProductRequestDto(
                    ProductType.DESKTOP, testSerial, "Apple",
                    new BigDecimal("129999"), 10,
                    "ALL_IN_ONE", null, null, null
            );

            // When & Then
            assertThatThrownBy(() -> productService.updateProduct(testId, differentTypeRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Cannot change product type");

            verify(productRepository).findById(testId);
            verify(productRepository, never()).save(any(Product.class));
        }
    }
}