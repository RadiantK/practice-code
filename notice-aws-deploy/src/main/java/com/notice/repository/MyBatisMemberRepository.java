package com.notice.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.notice.domain.Member;
import com.notice.repository.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {

	private final MemberMapper memberMapper;
	
	@Override
	public Optional<Member> findById(String memberId) {
		return memberMapper.findById(memberId);
	}

	@Override
	public Optional<Member> findByLoginId(String loginId) {
		return memberMapper.findByLoginId(loginId);
	}

	
	
}
