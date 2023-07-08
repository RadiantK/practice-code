package com.notice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeResponse {
	
	private Long id;
	
	private String title;
	
	private String content;
	
	private String createdAt;
	
}
