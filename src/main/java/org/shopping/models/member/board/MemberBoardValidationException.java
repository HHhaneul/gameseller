package org.shopping.models.member.board;

import org.shopping.commons.exception.CommonException;
import org.springframework.http.HttpStatus;

public class MemberBoardValidationException extends CommonException {
    public MemberBoardValidationException(String code) {
        super(bundleValidation.getString(code), HttpStatus.BAD_REQUEST);
    }
}
