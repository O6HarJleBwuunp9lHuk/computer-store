package org.test.h2o.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.dto.ProductResponseDto;
import org.test.h2o.exception.DuplicateSerialException;
import org.test.h2o.exception.ProductNotFoundException;
import org.test.h2o.mapper.ProductMapper;
import org.test.h2o.model.Product;
import org.test.h2o.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ValidationService validationService;

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        log.debug("Creating new product with serial: {}", requestDto.serialNumber());

        validationService.validate(requestDto);
        validateUniqueSerial(requestDto.serialNumber());

        Product product = createProductEntity(requestDto);
        Product saved = saveProduct(product);

        log.info("Product created with id: {}", saved.getId());
        return productMapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductResponseDto updateProduct(String id, ProductRequestDto requestDto) {
        log.debug("Updating product with id: {}", id);

        validationService.validate(requestDto);

        Product existing = findProductById(id);
        validationService.validateProductTypeUnchanged(existing.getType(), requestDto.type());

        updateProductFields(existing, requestDto);
        Product updated = saveProduct(existing);

        log.info("Product updated with id: {}", updated.getId());
        return productMapper.toResponse(updated);
    }

    @Cacheable(value = "productsByType", key = "#type")
    public List<ProductResponseDto> getProductsByType(String type) {
        String normalizedType = validationService.validateAndNormalizeType(type);
        log.debug("Fetching products by type: {}", normalizedType);

        return productRepository.findByType(normalizedType)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponseDto getProductById(String id) {
        log.debug("Fetching product by id: {}", id);
        Product product = findProductById(id);
        return productMapper.toResponse(product);
    }

    private void validateUniqueSerial(String serialNumber) {
        if (productRepository.existsBySerialNumber(serialNumber)) {
            throw new DuplicateSerialException(serialNumber);
        }
    }

    private Product findProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private Product createProductEntity(ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        product.setCreationTime();
        return product;
    }

    private void updateProductFields(Product product, ProductRequestDto dto) {
        productMapper.updateEntity(product, dto);
        product.updateModificationTime();
    }

    private Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}