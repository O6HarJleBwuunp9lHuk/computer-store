package org.test.h2o.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ProductType;
import org.test.h2o.mapper.ProductMapper;
import org.test.h2o.model.Product;
import org.test.h2o.repository.ProductRepository;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public abstract class ProductServiceTestBase {

    @Mock
    protected ProductRepository productRepository;

    @Mock
    protected ProductMapper productMapper;

    @Mock
    protected ValidationService validationService;

    @InjectMocks
    protected ProductService productService;

    protected Product product;
    protected Product savedProduct;
    protected ProductRequestDto requestDto;
    protected String testId = "65f7a1b2c3d4e5f6g7h8i9j0";
    protected String testSerial = "SN-TEST-001";

    @BeforeEach
    void setUpBase() {
        product = new Product();
        product.setSerialNumber(testSerial);
        product.setType(ProductType.LAPTOP);
        product.setManufacturer("Apple");
        product.setPrice(new BigDecimal("129999"));
        product.setStockQuantity(10);
        product.setScreenSize(15);

        savedProduct = new Product();
        savedProduct.setId(testId);
        savedProduct.setSerialNumber(testSerial);
        savedProduct.setType(ProductType.LAPTOP);
        savedProduct.setManufacturer("Apple");
        savedProduct.setPrice(new BigDecimal("129999"));
        savedProduct.setStockQuantity(10);
        savedProduct.setScreenSize(15);

        requestDto = new ProductRequestDto(
                ProductType.LAPTOP, testSerial, "Apple",
                new BigDecimal("129999"), 10,
                null, 15, null, null
        );
    }
}