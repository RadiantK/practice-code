package com.notice.dto;

import lombok.Data;

@Data
public class NoticeListRequest {

	private int page;
	
	private String type;
	
	private String word;
}
