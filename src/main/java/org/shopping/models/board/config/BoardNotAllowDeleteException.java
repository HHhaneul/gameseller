package org.shopping.models.board.config;

import org.shopping.commons.CommonException;
import org.springframework.http.HttpStatus;

public class BoardNotAllowDeleteException extends CommonException {
    public BoardNotAllowDeleteException() {
        super(bundleValidation.getString("Validation.board.NotAllowDelete"), HttpStatus.UNAUTHORIZED);
    }
}