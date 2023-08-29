package org.shopping.models.member;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.shopping.controllers.admins.MemberSearch;
import org.shopping.entities.Member;
import org.shopping.entities.QMember;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class MemberListService {

    private final MemberRepository memberRepository;

    public Page<Member> gets(MemberSearch memberSearch) {
        QMember member = QMember.member;

        BooleanBuilder andBuilder = new BooleanBuilder();
        BooleanBuilder orBuilder = new BooleanBuilder();

        int page = memberSearch.getPage();
        int limit = memberSearch.getLimit();

        page = page < 1 ? 1 : page;
        limit = limit < 1 ? 20 : limit;
        /** 검색 조건 처리 S */
        String sopt = memberSearch.getSopt();
        String skey = memberSearch.getSkey();
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            skey = skey.trim();
            sopt = sopt.trim();

            /* 통합 검색 - 회원 아이디, 회원명 */
            if (sopt.equals("all")) {
                orBuilder.or(member.userId.contains(skey))
                        .or(member.userNm.contains(skey));
                andBuilder.and(orBuilder);

                /* 회원 아이디 */
            } else if (sopt.equals("userId")) {
                andBuilder.and(member.userId.contains(skey));

                /* 회원명 */
            } else if (sopt.equals("userNm")) {
                andBuilder.and(member.userNm.contains(skey));
            }
        }
        /** 검색 조건 처리 E */
        Pageable pageable = PageRequest.of(page-1,limit, Sort.by(Sort.Order.desc("createdAt")));
        Page<Member> data = memberRepository.findAll(andBuilder, pageable);


        return data;
    }

}
