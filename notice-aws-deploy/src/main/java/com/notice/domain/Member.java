package com.notice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

	private Long memberId;
	
	private String loginId;
	
	private String password;
	
	private Long lastLoginDate;
}
