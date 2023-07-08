package com.notice.repository.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.notice.domain.Member;

@Mapper
public interface MemberMapper {

	Optional<Member> findById(String memberId);
	
	Optional<Member> findByLoginId(String loginId);
}
