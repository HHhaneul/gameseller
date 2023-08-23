package org.shopping.models.member.board;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.validators.Validator;
import org.shopping.controllers.members.MemberBoardForm;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberBoardSaveValidator  implements Validator<MemberBoardForm> {

    private final BoardDataRepository repository;

    @Override
    public void check(MemberBoardForm memberBoardForm) {
        Long id = memberBoardForm.getId();
        String mode = memberBoardForm.getMode();
        String subject = memberBoardForm.getSubject();
        String content = memberBoardForm.getContent();

        /* 게시글 수정인 경우 */
        if (mode != null && mode.equals("update")) {
            if (id == null) { // 게시글 번호 필수 체크
                nullCheck(id, new MemberBoardValidationException("잘못된 접근입니다."));
            }

            /* 게시글 등록 여부 체크 */

            if (!repository.exists(id)) {
                System.out.println("0--a-a-");
                throw new MemberBoardValidationException("등록되지 않은 게시글 입니다.");
            }

            /*repository.exists2(id).orElseThrow(() -> new MemberBoardValidationException("등록되지 않은 게시글 입니다."));*/
        }

        requiredCheck(subject, new MemberBoardValidationException("제목을 입력하세요."));
        requiredCheck(content, new MemberBoardValidationException("내용을 입력하세요."));
    }
}
