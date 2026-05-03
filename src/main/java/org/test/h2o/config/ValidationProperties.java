package org.test.h2o.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Маппинг конфигурации валидации из application.yml.
 *
 * <p>Свойства префикса: app.validation
 * <p>Все свойства обязательны. При отсутствии в конфигурации приложение не запустится.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Setter
@Getter
public class ValidationProperties {

    private Desktop desktop;
    private Laptop laptop;
    private Serial serial;
    private Manufacturer manufacturer;
    private Price price;
    private Stock stock;
    private ScreenSize screenSize;
    private Diagonal diagonal;
    private VolumeGb volumeGb;

    @PostConstruct
    public void validate() {
        Map.of(
                        "app.validation.serial", serial,
                        "app.validation.manufacturer", manufacturer,
                        "app.validation.price", price,
                        "app.validation.stock", stock,
                        "app.validation.screen-size", screenSize,
                        "app.validation.diagonal", diagonal,
                        "app.validation.volume-gb", volumeGb
                ).entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .findFirst()
                .ifPresent(entry -> {
                    throw new IllegalStateException(entry.getKey() + " is required");
                });
    }

    @Setter
    @Getter
    public static class Desktop {
        private List<String> formFactors;
    }

    @Setter
    @Getter
    public static class Laptop {
        private List<Integer> screenSizes;
    }

    @Setter
    @Getter
    public static class Serial {
        private int minLength;
        private int maxLength;
        private String pattern;

    }

    @Setter
    @Getter
    public static class Manufacturer {
        private int minLength;
        private int maxLength;

    }

    @Setter
    @Getter
    public static class Price {
        private String min;
        private String max;

    }

    @Setter
    @Getter
    public static class Stock {
        private int min;
        private int max;

    }

    @Setter
    @Getter
    public static class ScreenSize {
        private int min;
        private int max;

    }

    @Setter
    @Getter
    public static class Diagonal {
        private int min;
        private int max;

    }

    @Setter
    @Getter
    public static class VolumeGb {
        private int min;
        private int max;
    }
}