package com.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeEditDto {

	private Long noticeId;
	
	private String title;
	
	private String content;
}
