package org.shopping.models.member.board;

import org.shopping.commons.CommonException;
import org.springframework.http.HttpStatus;

public class MemberBoardDataNotExistsException extends CommonException {
    public MemberBoardDataNotExistsException() {
        super(bundleValidation.getString("Validation.boardData.notExists"), HttpStatus.BAD_REQUEST);
    }
}