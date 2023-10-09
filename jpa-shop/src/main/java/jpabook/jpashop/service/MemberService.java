package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.dto.MemberResponse;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param memberDto
     * @return
     */
    @Transactional
    public Long join(MemberDto memberDto) {
        validateDuplicateMember(memberDto.getName()); // 중복 회원 검증

        Member member = memberDto.toEntity();

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(String name) {
        List<Member> findMembers = memberRepository.findByName(name);

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     * @return
     */
    public List<MemberResponse> findMembers() {
        List<Member> findMembers = memberRepository.findAll();

        return findMembers.stream()
                .map(MemberResponse::of)
                .collect(Collectors.toList());
    }

    /**
     * 회원 단건 조회
     * @param memberId
     * @return
     */
    public MemberResponse findMember(Long memberId) {
        return MemberResponse.of(memberRepository.findById(memberId));
    }

    @Transactional
    public void update(Long id, String name) {
        Member findMember = memberRepository.findById(id);
        findMember.editInfo(name);
    }
}
