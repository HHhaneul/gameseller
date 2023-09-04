package org.shopping.models.member;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.commons.validators.RequiredValidator;
import org.shopping.entities.Member;
import org.shopping.entities.QMember;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberDeleteService implements RequiredValidator {

    private final MemberRepository memberRepository;
    private final Utils utils;

    public void delete(Long userNo) {
        Member member = memberRepository.findById(userNo).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
        memberRepository.flush();
    }

    public void delete(Long[] userNo) {
        QMember member = QMember.member;
        if (userNo == null || userNo.length == 0) return;

        List<Member> items = (List<Member>)memberRepository.findAll(member.userNo.in(userNo));

        if(items == null || items.isEmpty()) return;

        memberRepository.deleteAll(items);
        memberRepository.flush();
    }

    public void delete(Member member) {
        List<Member> items = new ArrayList<>();
        List<Integer> chks = member.getChkNo();
        nullCheck(chks, utils.getMessage("NotSeleted.delete", "validation"));

        for (Integer chk : chks) {
            Long userNo = Long.valueOf(utils.getParam("userNo_" + chk));
            Member item = memberRepository.findById(userNo).orElse(null);
            if(item == null) continue;
            items.add(item);
        }
        memberRepository.deleteAll(items);
        memberRepository.flush();
    }
}
