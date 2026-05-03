package org.test.h2o.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.test.h2o.config.ValidationProperties;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ProductType;

import java.util.Set;

/**
 * Валидатор для настольных компьютеров (DESKTOP).
 * Проверяет корректность форм-фактора.
 * Использует конфигурацию из application.yml.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class DesktopValidator implements TypeSpecificValidator {

    private final ValidationProperties props;

    @Override
    public void validate(ProductRequestDto dto) {
        if (dto.formFactor() == null) {
            throw new IllegalArgumentException("Form factor is required");
        }

        Set<String> validFormFactors = Set.copyOf(props.getDesktop().getFormFactors());

        String formFactorValue = dto.formFactor();

        if (!validFormFactors.contains(formFactorValue)) {
            throw new IllegalArgumentException(
                    String.format("Invalid form factor: %s. Valid values: %s",
                            formFactorValue, validFormFactors)
            );
        }
    }

    @Override
    public ProductType getType() {
        return ProductType.DESKTOP;
    }
}