package org.shopping.models.member.board;

import org.shopping.commons.MemberUtil;
import org.shopping.commons.validators.*;
import org.shopping.controllers.members.MemberBoardForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class MemberBoardValidator implements Validator<MemberBoardForm>, RequiredValidator, LengthValidator {

    @Autowired
    private MemberUtil memberUtil;
    @Override
    public void check(MemberBoardForm memberBoardForm) {
        requiredCheck(memberBoardForm.getBId(), new MemberBoardValidationException("NotBlank.memberFormForm.bid"));
        requiredCheck(memberBoardForm.getGid(), new MemberBoardValidationException("NotBlank.memberFormForm.gid"));
        requiredCheck(memberBoardForm.getPoster(), new MemberBoardValidationException("NotBlank.memberFormForm.poster"));
        requiredCheck(memberBoardForm.getSubject(), new MemberBoardValidationException("NotBlank.memberFormForm.subject"));
        requiredCheck(memberBoardForm.getContent(), new MemberBoardValidationException("NotBlank.memberFormForm.content"));

        /* 비회원 - 비회원 비밀번호 체크 */
        if (!memberUtil.isLogin()) {
            requiredCheck(memberBoardForm.getGuestPw(), new MemberBoardValidationException("NotBlank.memberBoardForm.guestPw"));

            /* 비회원 비밀번호 자리수는 6자리 이상 */
            lengthCheck(memberBoardForm.getGuestPw(), 6, new MemberBoardValidationException("Size.memberBoardForm.guestPw"));
        }
    }
}