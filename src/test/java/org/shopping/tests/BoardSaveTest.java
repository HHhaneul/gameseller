package org.shopping.tests;

import org.junit.jupiter.api.*;
import org.shopping.controllers.admins.BoardForm;
import org.shopping.controllers.members.*;
import org.shopping.entities.Board;
import org.shopping.models.board.config.*;
import org.shopping.models.member.MemberSaveService;
import org.shopping.models.member.board.MemberBoardSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class BoardSaveTest {

    @Autowired
    private BoardConfigSaveService saveService;
    @Autowired
    private BoardConfigSaveService configSaveService;
    @Autowired
    private BoardConfigInfoService configInfoService;
    @Autowired
    private MemberBoardSaveService boardSaveService;
    @Autowired
    private MemberSaveService memberSaveService;

    private Board board;
    private JoinForm joinForm;

    @BeforeEach
    @Transactional
    void init() {
        // 게시판 설정 추가
        org.shopping.controllers.admins.BoardForm boardForm = new org.shopping.controllers.admins.BoardForm();
        boardForm.setBId("ssss");
        boardForm.setBName("자유게시판");
        configSaveService.save(boardForm);
        board = configInfoService.get(boardForm.getBId(), true);

        // 회원 가입 추가
        joinForm = JoinForm.builder()
                .userId("user01")
                .userPw("aA!123456")
                .userPwRe("aA!123456")
                .email("user01@test.org")
                .userNm("사용자01")
                .mobile("01000000000")
                .agrees(new boolean[]{true})
                .build();
        memberSaveService.save(joinForm);
    }


    private MemberBoardForm getGuestBoardForm() {

            return MemberBoardForm.builder()
                    .bId(board.getBId())
                    .guestPw("12345678")
                    .poster("비회원")
                    .subject("제목!")
                    .content("내용!")
                    .category(board.getCategories() == null ? null : board.getCategories()[0])
                    .build();

    }

/*    @WithMockUser(username="user01", password="aA!123456")
    private MemberBoardForm getCommonBoardForm() {
        System.out.println(board.getBId());
        return MemberBoardForm.builder()
                .bId(board.getBId())
                .gid(UUID.randomUUID().toString())
                .poster(joinForm.getUserNm())
                .subject("제목!")
                .content("내용!")
                .category(board.getCategories() == null ? null : board.getCategories()[0])
                .build();
    }*/

    @Test
    @WithMockUser(username="user01", password="aA!123456")
    void test(){
        MemberBoardForm memberBoardForm = MemberBoardForm.builder()
                .bId(board.getBId())
                .gid(UUID.randomUUID().toString())
                .poster(joinForm.getUserNm())
                .subject("제목!")
                .content("내용!")
                .category(board.getCategories() == null ? null : board.getCategories()[0])
                .build();

        boardSaveService.save(memberBoardForm);
        /*assertDoesNotThrow(() -> {
            boardSaveService.save(getGuestBoardForm());
        });*/
    }
}
