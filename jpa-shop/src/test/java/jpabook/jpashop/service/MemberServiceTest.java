package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.dto.MemberRequest;
import jpabook.jpashop.dto.MemberResponse;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @DisplayName("회원가입")
    @Test
    void join() {
        // given
        MemberRequest memberRequest = createMemberRequest("kang");

        // when
        Long savedId = memberService.join(memberRequest.toMemberDto());

        // then
        assertThat(savedId).isNotNull();
    }

    @DisplayName("중복된 이름으로 가입 시 예외 발생")
    @Test
    void duplicateJoin() {
        // given
        MemberRequest memberRequest1 = createMemberRequest("kang");
        MemberRequest memberRequest2 = createMemberRequest("kang");

        // when & then
        memberService.join(memberRequest1.toMemberDto());
        assertThatThrownBy(() -> memberService.join(memberRequest2.toMemberDto()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 회원입니다.");
    }

    @DisplayName("회원의 아이디로 조회")
    @Test
    void findMember() {
        MemberRequest memberRequest = createMemberRequest("kang");
        Long savedId = memberService.join(memberRequest.toMemberDto());

        // when
        MemberResponse result = memberService.findMember(savedId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedId);
    }

    private MemberRequest createMemberRequest(String name) {
        String zipcode = "01010";
        String city = "서울시 구로구";
        String street = "빌라 5층";

        return MemberRequest.builder()
                .name(name)
                .zipcode(zipcode)
                .city(city)
                .street(street)
                .build();
    }
}