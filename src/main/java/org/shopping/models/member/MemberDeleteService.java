package org.shopping.models.member;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.commons.validators.RequiredValidator;
import org.shopping.controllers.members.JoinForm;
import org.shopping.entities.Member;
import org.shopping.entities.QMember;
import org.shopping.repositories.MemberListRepository;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberDeleteService implements RequiredValidator {

    private final MemberListRepository memberListRepository;
    private final Utils utils;

    public void delete(Long userNo) {
        Member member = memberListRepository.findById(userNo)
                .orElseThrow(MemberNotFoundException::new);
        memberListRepository.delete(member);
    }

    public void delete(Long[] userNo) {
        QMember member = QMember.member;
        if (userNo == null || userNo.length == 0) return;

        List<Member> items = (List<Member>) memberListRepository.findByUserNo(userNo);

        if (items == null || items.isEmpty()) return;

        memberListRepository.deleteAll(items);
    }

    public void delete(JoinForm form) {
        List<Member> items = new ArrayList<>();
        List<Integer> chks = form.getChkNo();
        nullCheck(chks, utils.getMessage("NotSeleted.delete", "validation"));

        for (Integer chk : chks) {
            Long userNo = Long.valueOf(utils.getParam("userNo_" + chk));
            // findById 메소드 사용 및 예외 처리 추가
            Member item = memberListRepository.findById(userNo)
                    .orElse(null);
            if (item == null) continue;
            items.add(item);
        }
        memberListRepository.deleteAll(items);
    }
}
