package org.test.h2o.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.test.h2o.config.ValidationProperties;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ProductType;

@Component
@RequiredArgsConstructor
public class MonitorValidator implements TypeSpecificValidator {

    private final ValidationProperties props;

    @Override
    public void validate(ProductRequestDto dto) {
        if (dto.diagonal() == null) {
            throw new IllegalArgumentException("Monitor requires diagonal");
        }

        int min = props.getDiagonal().getMin();
        int max = props.getDiagonal().getMax();

        if (dto.diagonal() < min || dto.diagonal() > max) {
            throw new IllegalArgumentException(
                    String.format("Diagonal must be between %d and %d inches", min, max)
            );
        }
    }

    @Override
    public ProductType getType() {
        return ProductType.MONITOR;
    }
}