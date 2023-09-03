package org.shopping.models.order;

import org.shopping.commons.CommonException;
import org.springframework.http.HttpStatus;

public class CartItemNotFoundException extends CommonException {
    public CartItemNotFoundException() {
        super(bundleValidation.getString("NotFound.cartItem"), HttpStatus.BAD_REQUEST);
    }
}
