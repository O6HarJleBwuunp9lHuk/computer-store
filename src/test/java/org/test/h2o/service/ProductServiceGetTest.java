package org.test.h2o.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.test.h2o.dto.ProductResponseDto;
import org.test.h2o.enam.ProductType;
import org.test.h2o.exception.ProductNotFoundException;
import org.test.h2o.model.Product;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


class ProductServiceGetTest extends ProductServiceTestBase {

    @Nested
    @DisplayName("getProductById")
    class GetProductById {

        @Test
        @DisplayName("returnsProduct, когда продукт найден")
        void returnsProduct_whenProductFound() {
            // Given
            when(productRepository.findById(testId)).thenReturn(Optional.of(product));
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

            // When
            ProductResponseDto result = productService.getProductById(testId);

            // Then
            assertThat(result).isNotNull();
            verify(productRepository).findById(testId);
        }

        @Test
        @DisplayName("throwsProductNotFoundException, когда продукт не найден")
        void throwsProductNotFoundException_whenProductNotFound() {
            // Given
            when(productRepository.findById(testId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> productService.getProductById(testId))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getProductsByType")
    class GetProductsByType {

        @Test
        @DisplayName("returnsProductList, когда тип валиден")
        void returnsProductList_whenTypeIsValid() {
            // Given
            ProductType laptopType = ProductType.LAPTOP;
            when(productRepository.findByType("LAPTOP")).thenReturn(List.of(product));
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

            // When
            List<ProductResponseDto> result = productService.getProductsByType(laptopType);

            // Then
            assertThat(result).isNotEmpty();
            verify(productRepository).findByType("LAPTOP");
        }

        @Test
        @DisplayName("возвращает пустой список, когда тип валиден но продуктов нет")
        void returnsEmptyList_whenNoProductsFound() {
            // Given
            ProductType laptopType = ProductType.LAPTOP;
            when(productRepository.findByType("LAPTOP")).thenReturn(List.of());

            // When
            List<ProductResponseDto> result = productService.getProductsByType(laptopType);

            // Then
            assertThat(result).isEmpty();
            verify(productRepository).findByType("LAPTOP");
        }
    }
}