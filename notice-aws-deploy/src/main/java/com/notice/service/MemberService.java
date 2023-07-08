package com.notice.service;

import org.springframework.stereotype.Service;

import com.notice.domain.Member;
import com.notice.dto.MemberLoginRequest;
import com.notice.dto.MemberLoginResponse;
import com.notice.exception.MemberNotFoundException;
import com.notice.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
	public MemberLoginResponse login(MemberLoginRequest request) {
		Member findMember = getMember(request.getId());
		
		if (!findMember.getPassword().equals(request.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		
		return MemberLoginResponse.builder()
				.memberId(findMember.getMemberId())
				.loginId(findMember.getLoginId())
				.build();
	}
	
	private Member getMember(String id) {
		return memberRepository.findByLoginId(id)
				.orElseThrow(() -> new MemberNotFoundException("요청하신 회원 정보는 일치하지 않습니다."));
	}
}
