package org.test.h2o.exception;

import static org.test.h2o.enam.ErrorCode.DUPLICATE_SERIAL_NUMBER;

/**
 * Исключение, выбрасываемое при попытке создать продукт
 * с уже существующим серийным номером.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
public class DuplicateSerialException extends BaseException {

    public DuplicateSerialException(String serialNumber) {
        super(
                String.format("Product with serial number '%s' already exists", serialNumber),
                DUPLICATE_SERIAL_NUMBER
        );
    }
}