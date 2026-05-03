package org.test.h2o.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.dto.ProductResponseDto;
import org.test.h2o.service.ProductService;

import java.util.List;

/**
 * REST контроллер для управления продуктами.
 * Предоставляет полный CRUD API для товаров компьютерного магазина.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ProductController {

    private final ProductService productService;

    /**
     * Создаёт новый продукт.
     *
     * @param requestDto данные продукта
     * @return созданный продукт с HTTP статусом 201
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        log.info("POST /api/products - Creating product with serial: {}", requestDto.serialNumber());
        return productService.createProduct(requestDto);
    }

    /**
     * Обновляет существующий продукт.
     *
     * @param id идентификатор продукта
     * @param requestDto обновлённые данные
     * @return обновлённый продукт
     */
    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequestDto requestDto
    ) {
        log.info("PUT /api/products/{} - Updating product", id);
        return productService.updateProduct(id, requestDto);
    }

    /**
     * Возвращает все продукты указанного типа.
     *
     * @param type тип продукта (DESKTOP, LAPTOP, MONITOR, HDD)
     * @return список продуктов
     */
    @GetMapping("/types/{type}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByType(
            @PathVariable
            @Pattern(regexp = "DESKTOP|LAPTOP|MONITOR|HDD",
                    message = "Type must be one of: DESKTOP, LAPTOP, MONITOR, HDD")
            String type
    ) {
        log.info("GET /api/products/types/{} - Fetching products by type", type);
        List<ProductResponseDto> products = productService.getProductsByType(type);
        return ResponseEntity.ok(products);
    }

    /**
     * Возвращает продукт по идентификатору.
     *
     * @param id идентификатор продукта
     * @return продукт
     */
    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable String id) {
        log.info("GET /api/products/{} - Fetching product by id", id);
        return productService.getProductById(id);
    }
}