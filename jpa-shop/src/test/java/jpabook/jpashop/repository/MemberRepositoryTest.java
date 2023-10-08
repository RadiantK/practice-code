package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 저장")
    @Test
    void save() {
        // given
        Member member = createMember("kang");

        // when
        Long savedId = memberRepository.save(member);

        // then
        assertThat(savedId).isNotNull();
        assertThat(member.getId()).isEqualTo(savedId);
    }

    @DisplayName("회원 전체 조회")
    @Test
    void findAll() {
        // given
        Member member1 = createMember("kang");
        Member member2 = createMember("kim");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> findMembers = memberRepository.findAll();

        // then
        assertThat(findMembers).hasSize(2)
                .extracting("name", "address")
                .containsExactlyInAnyOrder(
                        tuple("kang", null),
                        tuple("kim", null)
                );
    }

    @DisplayName("회원 아이디로 단건 조회")
    @Test
    void findById() {
        // given
        Member member = createMember("kang");
        Long savedId = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(savedId);

        // then
        assertThat(findMember).isNotNull();
        assertThat(findMember).isEqualTo(member);
        assertThat(findMember.getId()).isEqualTo(savedId);
    }

    @DisplayName("회원 이름으로 조회")
    @Test
    void findByName() {
        Member member1 = createMember("kang");
        Member member2 = createMember("kim");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> findMembers = memberRepository.findByName("kang");

        // then
        assertThat(findMembers).hasSize(1)
                .extracting("name", "address")
                .containsExactlyInAnyOrder(
                        tuple("kang", null)
                );
    }

    private Member createMember(String name) {
        return Member.builder()
                .name(name)
                .build();
    }

}