package com.notice.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class MemberLoginRequest {

	@NotBlank(message = "아이디를 입력하세요.")
	private String id;
	@NotBlank(message = "비밀번호를 입력하세요")
	private String password;
}
