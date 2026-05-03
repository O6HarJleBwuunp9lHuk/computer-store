package org.test.h2o.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.test.h2o.config.ValidationProperties;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ProductType;
import org.test.h2o.service.validator.DesktopValidator;
import org.test.h2o.service.validator.HddValidator;
import org.test.h2o.service.validator.LaptopValidator;
import org.test.h2o.service.validator.MonitorValidator;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@DisplayName("ProductController Integration Tests")
class ProductControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7"))
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class TestConfig {
        @Bean
        public LaptopValidator laptopValidator(ValidationProperties props) {
            return new LaptopValidator(props);
        }

        @Bean
        public DesktopValidator desktopValidator(ValidationProperties props) {
            return new DesktopValidator(props);
        }

        @Bean
        public MonitorValidator monitorValidator(ValidationProperties props) {
            return new MonitorValidator(props);
        }

        @Bean
        public HddValidator hddValidator(ValidationProperties props) {
            return new HddValidator(props);
        }
    }

    @BeforeEach
    void cleanDatabase() {
        mongoTemplate.dropCollection("products");
    }

    @Nested
    @DisplayName("POST /api/products")
    class CreateProduct {

        @Test
        @DisplayName("creates product and returns 201")
        void createsProductAndReturns201() throws Exception {
            ProductRequestDto request = new ProductRequestDto(
                    ProductType.LAPTOP,
                    "SN-INT-001",
                    "Apple",
                    new BigDecimal("129999"),
                    10,
                    null,
                    15,
                    null,
                    null
            );

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.serialNumber").value("SN-INT-001"))
                    .andExpect(jsonPath("$.type").value("LAPTOP"))
                    .andExpect(jsonPath("$.id").exists());
        }

        @Test
        @DisplayName("returns 409 when serial number already exists")
        void returns409_whenSerialNumberAlreadyExists() throws Exception {
            ProductRequestDto request = new ProductRequestDto(
                    ProductType.LAPTOP,
                    "SN-DUPLICATE",
                    "Apple",
                    new BigDecimal("129999"),
                    10,
                    null,
                    15,
                    null,
                    null
            );

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("GET /api/products/{id}")
    class GetProductById {

        @Test
        @DisplayName("returns product when exists")
        void returnsProduct_whenExists() throws Exception {
            ProductRequestDto request = new ProductRequestDto(
                    ProductType.LAPTOP,
                    "SN-GET-001",
                    "Apple",
                    new BigDecimal("129999"),
                    10,
                    null,
                    15,
                    null,
                    null
            );

            String response = mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String id = objectMapper.readTree(response).get("id").asText();

            mockMvc.perform(get("/api/products/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.serialNumber").value("SN-GET-001"));
        }

        @Test
        @DisplayName("returns 404 when product not found")
        void returns404_whenProductNotFound() throws Exception {
            mockMvc.perform(get("/api/products/non-existent-id"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/products/types/{type}")
    class GetProductsByType {

        @Test
        @DisplayName("returns products by type")
        void returnsProductsByType() throws Exception {
            ProductRequestDto laptop1 = new ProductRequestDto(
                    ProductType.LAPTOP,
                    "SN-TYPE-001",
                    "Apple",
                    new BigDecimal("129999"),
                    10,
                    null,
                    15,
                    null,
                    null
            );
            ProductRequestDto laptop2 = new ProductRequestDto(
                    ProductType.LAPTOP,
                    "SN-TYPE-002",
                    "Dell",
                    new BigDecimal("89999"),
                    5,
                    null,
                    15,
                    null,
                    null
            );

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(laptop1)))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(laptop2)))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/products/types/LAPTOP"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Nested
    @DisplayName("PUT /api/products/{id}")
    class UpdateProduct {

        @Test
        @DisplayName("updates product and returns 200")
        void updatesProductAndReturns200() throws Exception {
            ProductRequestDto createRequest = new ProductRequestDto(
                    ProductType.LAPTOP,
                    "SN-UPDATE-001",
                    "Apple",
                    new BigDecimal("129999"),
                    10,
                    null,
                    15,
                    null,
                    null
            );

            String response = mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String id = objectMapper.readTree(response).get("id").asText();

            ProductRequestDto updateRequest = new ProductRequestDto(
                    ProductType.LAPTOP,
                    "SN-UPDATE-001",
                    "Apple",
                    new BigDecimal("99999"),
                    5,
                    null,
                    15,
                    null,
                    null
            );

            mockMvc.perform(put("/api/products/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.price").value(99999))
                    .andExpect(jsonPath("$.stockQuantity").value(5));
        }
    }
}