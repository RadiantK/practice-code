package jpabook.jpashop.controller;

import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @DisplayName("회원 등록 페이지")
    @Test
    void createForm() throws Exception {
        // given


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/members/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(MockMvcResultMatchers.view().name("members/createMemberForm"));
    }

    @DisplayName("회원 등록에 성공하면 메인 페이지로 리다이렉트 한다.")
    @Test
    void create() throws Exception {
        // given


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/members/new")
                .param("name", "kang")
                .param("city", "도시")
                .param("street", "거리")
                .param("zipcode", "10101")
        )
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
    }

}