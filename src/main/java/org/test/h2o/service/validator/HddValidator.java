package org.test.h2o.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.test.h2o.config.ValidationProperties;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ProductType;

@Component
@RequiredArgsConstructor
public class HddValidator implements TypeSpecificValidator {

    private final ValidationProperties props;

    @Override
    public void validate(ProductRequestDto dto) {
        if (dto.volumeGb() == null) {
            throw new IllegalArgumentException("HDD requires volumeGb");
        }

        int min = props.getVolumeGb().getMin();
        int max = props.getVolumeGb().getMax();

        if (dto.volumeGb() < min || dto.volumeGb() > max) {
            throw new IllegalArgumentException(
                    String.format("Volume must be between %d and %d GB", min, max)
            );
        }
    }

    @Override
    public ProductType getType() {
        return ProductType.HDD;
    }
}