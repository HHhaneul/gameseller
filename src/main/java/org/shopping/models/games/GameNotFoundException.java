package org.shopping.models.games;

import org.shopping.commons.CommonException;
import org.springframework.http.HttpStatus;

public class GameNotFoundException extends CommonException {
    public GameNotFoundException() {
        super(bundleValidation.getString("NotFound.game"), HttpStatus.BAD_REQUEST);
    }
}