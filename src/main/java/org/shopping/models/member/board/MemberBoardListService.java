package org.shopping.models.member.board;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.controllers.members.MemberBoardSearch;
import org.shopping.entities.MemberBoardData;
import org.shopping.entities.QMemberBoardData;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class MemberBoardListService {

    private final BoardDataRepository boardDataRepository;

    public Page<MemberBoardData> gets(MemberBoardSearch memberBoardSearch, String bId) {
        QMemberBoardData memberBoardData = QMemberBoardData.memberBoardData;

        BooleanBuilder andBuilder = new BooleanBuilder();
        BooleanBuilder orBuilder = new BooleanBuilder();

        int page = memberBoardSearch.getPage();
        int limit = memberBoardSearch.getLimit();

        page = Utils.getNumber(page, 1);
        limit = Utils.getNumber(limit, 20);
        /** 검색 조건 처리 S */
        String sopt = memberBoardSearch.getSopt();
        String skey = memberBoardSearch.getSkey();
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            skey = skey.trim();
            sopt = sopt.trim();

            /* 통합 검색 - 제목, 작성자 */
            if (sopt.equals("all")) {
                orBuilder.or(memberBoardData.subject.contains(skey))
                        .or(memberBoardData.poster.contains(skey));
                andBuilder.and(orBuilder);

                /* 제목 */
            } else if (sopt.equals("subject")) {
                System.out.println("뭐");
                andBuilder.and(memberBoardData.subject.contains(skey));

                /* 작성자 */
            } else if (sopt.equals("poster")) {
                andBuilder.and(memberBoardData.poster.contains(skey));

            } else if (sopt.equals("bId")) {
                andBuilder.and(memberBoardData.board.bId.eq(bId));
            }
        }
        /** 검색 조건 처리 E */

        /** 게시글 종류별 조회 */
        if (bId != null){
            andBuilder.and((memberBoardData.board.bId.eq(bId)));
        }

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<MemberBoardData> data = boardDataRepository.findAll(andBuilder, pageable);

        System.out.println("뭘까" + andBuilder);
        return data;
    }

    public Page<MemberBoardData> gets(MemberBoardSearch memberBoardSearch) {
        QMemberBoardData memberBoardData = QMemberBoardData.memberBoardData;

        BooleanBuilder andBuilder = new BooleanBuilder();
        BooleanBuilder orBuilder = new BooleanBuilder();

        int page = memberBoardSearch.getPage();
        int limit = memberBoardSearch.getLimit();

        page = page < 1 ? 1 : page;
        limit = limit < 1 ? 20 : limit;
        /** 검색 조건 처리 S */
        String sopt = memberBoardSearch.getSopt();
        String skey = memberBoardSearch.getSkey();
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            skey = skey.trim();
            sopt = sopt.trim();

            /* 통합 검색 - 제목, 작성자 */
            if (sopt.equals("all")) {
                orBuilder.or(memberBoardData.subject.contains(skey))
                        .or(memberBoardData.poster.contains(skey));
                andBuilder.and(orBuilder);

                /* 제목 */
            } else if (sopt.equals("subject")) {
                System.out.println("뭐");
                andBuilder.and(memberBoardData.subject.contains(skey));

                /* 작성자 */
            } else if (sopt.equals("poster")) {
                andBuilder.and(memberBoardData.poster.contains(skey));

            }
        }
        /** 검색 조건 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<MemberBoardData> data = boardDataRepository.findAll(andBuilder, pageable);

        System.out.println("뭘까" + andBuilder);
        return data;
    }

}
