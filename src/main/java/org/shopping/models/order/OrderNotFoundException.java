package org.shopping.models.order;

import org.shopping.commons.exception.AlertBackException;

public class OrderNotFoundException extends AlertBackException {

    public OrderNotFoundException() {
        super(bundleValidation.getString("NotFound.orderInfo"));
    }
}
