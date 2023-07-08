package com.notice.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notice.dto.CommentEditRequest;
import com.notice.dto.CommentListRequest;
import com.notice.dto.CommentListResponse;
import com.notice.dto.CommentRegistRequest;
import com.notice.dto.CommentRemoveRequest;
import com.notice.service.CommentService;
import com.notice.util.Constant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class ApiCommentController {

	private final CommentService commentService;
	
	@PostMapping("/regist")
	public ApiSingleDataResponse<String> registComment(
			@RequestBody CommentRegistRequest request,
			HttpServletRequest httpServletRequest
	) {
		
		log.info("request : {}", request);
		
		HttpSession session = httpServletRequest.getSession();
		Long loginId = (Long) session.getAttribute(Constant.LOGIN_MEMBER);
		
		if (loginId == null) {
			return new ApiSingleDataResponse<>("no-login");
		}
		
		commentService.addComment(request);
		
		return new ApiSingleDataResponse<>("ok");
	}
	
	@PostMapping
	public ApiSingleDataResponse<List<CommentListResponse>> commentList(
			@RequestBody CommentListRequest request
	) {
		log.info("CommentListRequest : {}", request);
		List<CommentListResponse> commentList = commentService.commentList(request);
		
		return new ApiSingleDataResponse<>(commentList);
	}
	
	@PostMapping("/edit")
	public ApiSingleDataResponse<String> editComment(
			@RequestBody CommentEditRequest request
	) {
		
		log.info("request : {}", request);
		commentService.editComment(request);
		
		return new ApiSingleDataResponse<String>("ok");
	}
	
	@PostMapping("/remove")
	public ApiSingleDataResponse<String> removeComment(
			@RequestBody CommentRemoveRequest request
	) {
		log.info("request : {}", request);
		commentService.removeComment(request);
		
		return new ApiSingleDataResponse<String>("ok");
	}
}
