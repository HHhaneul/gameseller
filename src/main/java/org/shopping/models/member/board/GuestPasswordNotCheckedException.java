package org.shopping.models.member.board;

import org.shopping.commons.exception.CommonException;
import org.springframework.http.HttpStatus;

public class GuestPasswordNotCheckedException extends CommonException {
    public GuestPasswordNotCheckedException() {
        super(bundleValidation.getString("GuestPw.notChecked"), HttpStatus.UNAUTHORIZED);
    }
}