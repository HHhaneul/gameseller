package org.shopping.models.member;

import org.shopping.commons.exception.CommonException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends CommonException {
    public MemberNotFoundException() {
        super(bundleError.getString("NotFound.Member"), HttpStatus.BAD_REQUEST);
    }
}

