package org.shopping.models.member.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.commons.MemberUtil;
import org.shopping.controllers.members.MemberBoardForm;
import org.shopping.entities.Board;
import org.shopping.entities.MemberBoardData;
import org.shopping.models.board.config.BoardConfigInfoService;
import org.shopping.repositories.member.BoardDataRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberBoardSaveService {

    private final MemberBoardValidator validator;
    private final MemberUtil memberUtil;
    private final BoardConfigInfoService configInfoService;
    private final BoardDataRepository repository;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final MemberBoardInfoService infoService;

    public void save(MemberBoardForm memberBoardForm) {
        validator.check(memberBoardForm);

        Long id = memberBoardForm.getId();
        Board board = configInfoService.get(memberBoardForm.getBId(), id == null ? "write" : "update");

        MemberBoardData memberBoardData = null;
        /* 게시글 추가 S */
        if (id == null) {
            String ip = request.getRemoteAddr();
            String ua = request.getHeader("User-Agent");
            memberBoardData = memberBoardData.builder()
                    .gid(memberBoardData.getGid())
                    .board(board)
                    .category(memberBoardData.getCategory())
                    .poster(memberBoardData.getPoster())
                    .subject(memberBoardData.getSubject())
                    .content(memberBoardData.getContent())
                    .ip(ip)
                    .ua(ua)
                    .build();

            /* Member */
            if (memberUtil.isLogin()) {
                memberBoardData.setMember(memberUtil.getEntity());
                
                /* Guest */
            }else {
                memberBoardData.setGuestPw(passwordEncoder.encode(memberBoardData.getGuestPw()));
            }
            /* 게시글 추가 E */

            /* 게시글 수정 S */
        }else {
            memberBoardData = repository.findById(memberBoardForm.getId()).orElseThrow(MemberBoardDataNotExistsException::new);
            memberBoardData.setPoster(memberBoardForm.getPoster());
            memberBoardData.setSubject(memberBoardForm.getSubject());
            memberBoardData.setContent(memberBoardForm.getContent());
            memberBoardData.setCategory(memberBoardForm.getCategory());
            String guestPw = memberBoardForm.getGuestPw();
            if (memberBoardData.getMember() == null && guestPw != null && !guestPw.isBlank()) {
                memberBoardData.setGuestPw(passwordEncoder.encode(guestPw));
        }
        /* 게시글 수정 E */
    }
        memberBoardData = repository.saveAndFlush(memberBoardData);
        memberBoardData.setId(memberBoardData.getId());
    }
}