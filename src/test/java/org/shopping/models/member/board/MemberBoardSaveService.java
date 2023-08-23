package org.shopping.models.member.board;

import lombok.RequiredArgsConstructor;
import org.shopping.controllers.members.MemberBoardForm;
import org.shopping.entities.MemberBoardData;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class MemberBoardSaveService {

    private final BoardDataRepository repository;
    private final MemberBoardSaveValidator validator;

    public void save(MemberBoardForm memberBoardForm) {
        save(memberBoardForm, null);
    }

    public void save(MemberBoardForm memberBoardForm, Errors errors) {
        if (errors != null && errors.hasErrors()) {
            return;
        }

        validator.check(memberBoardForm);

        MemberBoardData memberBoardData = null;
        // 게시글 수정
        String mode = memberBoardForm.getMode();
        Long id = memberBoardForm.getId();
        if (mode != null && mode.equals("update") && id != null) {
            memberBoardData = repository.findById(id).orElse(null);
            memberBoardData.setSubject(memberBoardForm.getSubject());
            memberBoardData.setContent(memberBoardForm.getContent());
        }

        if (memberBoardData == null) { // 게시글 추가
            memberBoardData = MemberBoardForm.of(memberBoardForm);
        }

        repository.saveAndFlush(memberBoardData);

        memberBoardForm.setId(memberBoardData.getId());
    }
}
