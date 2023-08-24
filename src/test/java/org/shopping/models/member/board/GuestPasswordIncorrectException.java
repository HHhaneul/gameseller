package org.shopping.models.member.board;

import org.shopping.commons.CommonException;
import org.springframework.http.HttpStatus;

public class GuestPasswordIncorrectException extends CommonException {
    public GuestPasswordIncorrectException() {
        super(bundleValidation.getString("GuestPw.incorrect"), HttpStatus.BAD_REQUEST);
    }
}