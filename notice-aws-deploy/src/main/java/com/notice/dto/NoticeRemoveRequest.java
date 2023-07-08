package com.notice.dto;

import lombok.Data;

@Data
public class NoticeRemoveRequest {

	private Long noticeId;
	
	private Long memberId;
}
