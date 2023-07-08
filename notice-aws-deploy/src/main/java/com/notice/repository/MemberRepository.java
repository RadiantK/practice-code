package com.notice.repository;

import java.util.Optional;

import com.notice.domain.Member;

public interface MemberRepository {

	Optional<Member> findById(String memberId);
	
	Optional<Member> findByLoginId(String loginId);
}
