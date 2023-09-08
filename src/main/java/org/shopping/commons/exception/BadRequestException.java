package org.shopping.commons.exception;

public class BadRequestException extends AlertBackException {
    public BadRequestException(String message) {

        super(message);
    }

    public BadRequestException() {
        super(bundleError.getString("BadRequest"));
    }
}