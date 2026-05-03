package org.test.h2o.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для создания бина ValidationProperties.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Configuration
public class ValidationConfig {

    /**
     * Создаёт бин ValidationProperties с настройками из application.yml.
     *
     * @return настроенный ValidationProperties
     */
    @Bean
    @ConfigurationProperties(prefix = "app.validation")
    public ValidationProperties validationProperties() {
        return new ValidationProperties();
    }
}