package org.shopping.models.member.board;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.shopping.controllers.members.MemberBoardSearch;
import org.shopping.entities.MemberBoardData;
import org.shopping.entities.QMemberBoardData;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

        page = page < 1 ? 1 : page;
        limit = limit < 1 ? 20 : limit;
        /** 검색 조건 처리 S */
        String sopt = memberBoardSearch.getSopt();
        String skey = memberBoardSearch.getSkey();
        System.out.println("검색" + memberBoardData.board.bId);
        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            skey = skey.trim();
            sopt = sopt.trim();

            /* 통합 검색 - bId, subject */
            if (sopt.equals("all")) {
                orBuilder.or(memberBoardData.board.bId.contains(skey))
                        .or(memberBoardData.subject.contains(skey));
                andBuilder.and(orBuilder);

                /* 게시판 아이디 bId */
            } else if (sopt.equals("bId")) {
                andBuilder.and(memberBoardData.board.bId.contains(skey));

                /* 게시판명 subject */
            } else if (sopt.equals("subject")) {
                andBuilder.and(memberBoardData.subject.contains(skey));
            }
        }
        /** 검색 조건 처리 E */
        if (bId != null){
            andBuilder.and((memberBoardData.board.bId.contains(bId)));
        }

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<MemberBoardData> data = boardDataRepository.findAll(andBuilder, pageable);

        return data;
    }
    public Page<MemberBoardData> gets(MemberBoardSearch memberBoardSearch) {
        return gets(memberBoardSearch, null);
    }

}
