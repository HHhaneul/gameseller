package org.shopping.tests.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.shopping.controllers.members.JoinForm;
import org.shopping.models.member.MemberSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class JoinTest {

    @Autowired
    private MockMvc mockMvc;

    private JoinForm joinForm;

    @Autowired
    private MemberSaveService saveService;

    @Test
    void test() throws Exception {
        JoinForm join = JoinForm.builder()
                .userId("user01")
                .userPw("_aA123456")
                .userPwRe("_aA123456")
                .userNm("사용자01")
                .email("user01@test.org")
                .mobile("01000000000")
                .build();

        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(join);

        mockMvc.perform(post("/member/join")
                        .contentType("application/json")
                        .content(json)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test2() throws Exception {
        JoinForm join = JoinForm.builder()
                .userId("user01")
                .userPw("")
                .userPwRe("_aA123456")
                .userNm("사용자01")
                .email("user01@test.org")
                .mobile("01000000000")
                .build();

        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(join);

        mockMvc.perform(post("/member/join")
                        .contentType("application/json")
                        .content(json)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
