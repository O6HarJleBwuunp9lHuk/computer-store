package org.test.h2o.exception;


import static org.test.h2o.enam.ErrorCode.PRODUCT_NOT_FOUND;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему продукту.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
public class ProductNotFoundException extends BaseException {

    public ProductNotFoundException(String id) {
        super(
                String.format("Product with id '%s' not found", id),
                PRODUCT_NOT_FOUND
        );
    }
}