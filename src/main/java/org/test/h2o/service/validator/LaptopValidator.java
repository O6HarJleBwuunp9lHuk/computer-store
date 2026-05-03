package org.test.h2o.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.test.h2o.config.ValidationProperties;
import org.test.h2o.dto.ProductRequestDto;
import org.test.h2o.enam.ProductType;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LaptopValidator implements TypeSpecificValidator {

    private final ValidationProperties props;

    @Override
    public void validate(ProductRequestDto dto) {
        if (dto.screenSize() == null) {
            throw new IllegalArgumentException("Screen size is required");
        }

        Set<Integer> validScreenSizes = Set.copyOf(props.getLaptop().getScreenSizes());

        if (!validScreenSizes.contains(dto.screenSize())) {
            throw new IllegalArgumentException(
                    String.format("Invalid screen size: %d. Valid values: %s",
                            dto.screenSize(), validScreenSizes)
            );
        }
    }

    @Override
    public ProductType getType() {
        return ProductType.LAPTOP;
    }
}