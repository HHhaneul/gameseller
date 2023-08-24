package org.shopping.tests;

import org.junit.jupiter.api.*;
import org.shopping.configs.SecurityConfig;
import org.shopping.controllers.members.JoinForm;
import org.shopping.controllers.members.MemberBoardForm;
import org.shopping.entities.Board;
import org.shopping.models.board.config.BoardConfigInfoService;
import org.shopping.models.board.config.BoardConfigSaveService;
import org.shopping.models.member.MemberSaveService;
import org.shopping.models.member.board.MemberBoardSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private MockMvc mockMvc;


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
                .userPw("_aA123456")
                .userPwRe("_aA123456")
                .email("user01@test.org")
                .userNm("사용자01")
                .mobile("01000000000")
                .agrees(new boolean[]{true})
                .build();
        memberSaveService.save(joinForm);
    }

    private void isLogin() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "user01");
        session.setAttribute("userPw", "_aA123456");
        mockMvc.perform(post("/member/login")
                        .param("userId", "user01")
                        .with(csrf().asHeader())
                        .param("userPw", "_aA123456")
                        .accept(MediaType.TEXT_HTML_VALUE)
                        .session(session));
    }


    private MemberBoardForm getGuestBoardForm() {

            return MemberBoardForm.builder()
                    .bId(board.getBId())
                    .gid(UUID.randomUUID().toString())
                    .guestPw("_aA123456")
                    .poster("비회원")
                    .subject("제목!")
                    .content("내용!")
                    .category(board.getCategories() == null ? null : board.getCategories()[0])
                    .build();

    }

    @WithMockUser(username="user01", password="aA!123456")
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
    }

    @Test
    void test() throws Exception {
        isLogin();
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

/*    @Test
    @WithAnonymousUser*/
/*    void registerGuestSuccessTest() {
        assertDoesNotThrow(() -> {
            boardSaveService.save(getGuestBoardForm());
        });
    }*/
}
