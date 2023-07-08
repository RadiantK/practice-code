package com.notice.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notice.dto.NoticeDetailRequset;
import com.notice.dto.NoticeDetailResponse;
import com.notice.dto.NoticeEditRequest;
import com.notice.dto.NoticeListRequest;
import com.notice.dto.NoticeRegistRequest;
import com.notice.dto.NoticeRemoveRequest;
import com.notice.dto.NoticeResponse;
import com.notice.service.NoticeService;
import com.notice.util.Constant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class ApiNoticeController {

	private final NoticeService noticeService;
	
	// 게시글 목록
	@PostMapping
	public NoticeDataResponse<NoticeResponse> noticeList(
			@RequestBody NoticeListRequest request
	) { 
		log.info("request : {}", request);
		
		List<NoticeResponse> noticeList = noticeService.noticeList(request);
		int noticeCount = noticeService.noticeCount(request);
		
		return new NoticeDataResponse<NoticeResponse>(
				noticeList, 
				request, 
				noticeCount);
	}
	
	// 게시글 등록
	@PostMapping(path = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiSingleDataResponse<String>> registNotice(
			@ModelAttribute NoticeRegistRequest request,
			HttpServletRequest httpServletRequest
	) {
		log.info("request : {}", request);
		
		HttpSession session = httpServletRequest.getSession();
		Long memberId = (Long) session.getAttribute(Constant.LOGIN_MEMBER);
		
		noticeService.registNotice(request, memberId);
		
		return new ResponseEntity<>(
				new ApiSingleDataResponse<>("ok"),
				HttpStatus.CREATED);
	}
	
	// 게시글 상세
	@PostMapping(path = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiSingleDataResponse<NoticeDetailResponse> detailNotice(
			@RequestBody NoticeDetailRequset requset
	) {
		log.info("id = {}", requset.getNoticeId());
		NoticeDetailResponse response = noticeService.detailNotice(requset.getNoticeId());
		
		return new ApiSingleDataResponse<>(response);
	}
	
	// 게시글 수정
	@PostMapping("/edit")
	public ApiSingleDataResponse<String> editNotice(
			@ModelAttribute NoticeEditRequest request
	) {
		log.info("edit request : {}", request);
		noticeService.edit(request);
		
		return new ApiSingleDataResponse<>("ok");
	}
	
	@PostMapping("/remove")
	public ApiSingleDataResponse<String> removeNotice(
			@RequestBody NoticeRemoveRequest request
	) {
		log.info("request : {}", request);
		
		noticeService.removeNotice(request);
		
		return new ApiSingleDataResponse<String>("ok");
	}
	
	@Data
	@NoArgsConstructor
	static class NoticeDataResponse<T> {
		
		private List<T> data;
		
		private NoticeListRequest listInfo;
		
		private int totalCount;

		public NoticeDataResponse(List<T> data, NoticeListRequest request, int totalCount) {
			this.data = data;
			this.listInfo = request;
			this.totalCount = totalCount;
		}
	}
}
