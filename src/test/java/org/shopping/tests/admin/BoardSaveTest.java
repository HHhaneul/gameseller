package org.shopping.tests.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.shopping.commons.constants.Role;
import org.shopping.controllers.admins.BoardForm;
import org.shopping.entities.Board;
import org.shopping.entities.Member;
import org.shopping.models.board.config.BoardConfigSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class BoardSaveTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardConfigSaveService saveService;

    private BoardForm boardForm;

    @BeforeEach
    void init(){
        for (int i = 1; i < 11; i++){
            boardForm = BoardForm.builder()
                    .bId("게시판ID" + i)
                    .bName("게시판명" + i)
                    .build();
        }
    }

    @Test @WithMockUser(authorities = "ADMIN")
    //@WithAnonymousUser
    void test1() throws Exception {

        boardForm = BoardForm.builder()
                .bId("게시판ID")
                .bName("게시판명")
                .build();

        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(boardForm);

        mockMvc.perform(post("/api/admin/board/register")
                .contentType("application/json")
                .content(json)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
