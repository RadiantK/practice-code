package com.notice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

	private Long noticeId;
	
	private String title;
	
	private String content;
	
	private String createdAt;
	
	private String updatedAt;
	
	private Long memberId;
	
	public Notice(String title, String content, Long memberId) {
		this.title = title;
		this.content = content;
		this.memberId = memberId;
	}

}