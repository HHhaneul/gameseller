package org.shopping.commons;

public class BadRequestException extends AlertBackException {
    public BadRequestException(String message) {

        super(message);
    }

    public BadRequestException() {
        super(bundleError.getString("BadRequest"));
    }
}