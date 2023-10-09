package jpabook.jpashop.api;

import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.dto.MemberRequest;
import jpabook.jpashop.dto.MemberResponse;
import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ApiMemberController.class)
class ApiMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @DisplayName("회원 조회")
    @Test
    void findMembers() throws Exception {
        // given
        BDDMockito.given(memberService.findMembers())
                .willReturn(List.of(
                        new MemberResponse(),
                        new MemberResponse()
                ));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/members"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.name()))
                ;

        BDDMockito.then(memberService).should(Mockito.times(1)).findMembers();
    }

    @DisplayName("회원 등록")
    @Test
    void saveMember() throws Exception {
        // given

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"kang\"}")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.name()));
    }

    @DisplayName("회원 등록 시 회원 이름이 빈문자이면 않으면 실패")
    @Test
    void saveMemberNotExistsMemberName() throws Exception {
        // given

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\"}")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("회원 이름은 필수 입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()));
    }
}