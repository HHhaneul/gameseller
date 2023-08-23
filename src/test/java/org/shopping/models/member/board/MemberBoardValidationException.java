package org.shopping.models.member.board;

import org.shopping.commons.CommonException;
import org.springframework.http.HttpStatus;

public class MemberBoardValidationException extends CommonException {

    /* 오류 시 400번 */
    public MemberBoardValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
