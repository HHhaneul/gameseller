package org.shopping.commons.validators;

import org.shopping.commons.BadRequestException;

/**
 * 필수항목 체크
 */
public interface RequiredValidator {
    default void requiredCheck(String str, RuntimeException e) {
        if (str == null || str.isBlank()) {
            throw e;
        }
    }

    default void nullCheck(Object obj, RuntimeException e) {
        if (obj == null) {
            throw e;
        }
    }

    default void nullCheck(Object obj, String message) {
        if (obj == null) {
            throw new BadRequestException(message);
        }
    }
    default void requiredCheck(String str, String message) {
        if (str == null || str.isBlank()) {
            throw new BadRequestException(message);
        }
    }

}
