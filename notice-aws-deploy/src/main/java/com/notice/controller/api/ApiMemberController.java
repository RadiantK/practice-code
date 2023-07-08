package com.notice.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notice.dto.MemberLoginRequest;
import com.notice.dto.MemberLoginResponse;
import com.notice.service.MemberService;
import com.notice.util.Constant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class ApiMemberController {

	private final MemberService  memberService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiSingleDataResponse<String>> memberLogin(
			@RequestBody MemberLoginRequest memberLoginRequest, 
			HttpServletRequest request
	) {
		
		log.info("memberLoginRequest = {}", memberLoginRequest);
		MemberLoginResponse loginMember = memberService.login(memberLoginRequest);
		
		HttpSession session = request.getSession();
		session.setAttribute(Constant.LOGIN_MEMBER, loginMember.getMemberId());
		
		return ResponseEntity
				.ok()
				.body(new ApiSingleDataResponse<>("ok"));
	}
}
