package com.notice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Comment {

	private Long commentId;
	
	private String content;
	
	private Long noticeId;

	private Long memberId;
	
	public Comment(String content, Long noticeId, Long memberId) {
		this.content = content;
		this.noticeId = noticeId;
		this.memberId = memberId;
	}
	
}
