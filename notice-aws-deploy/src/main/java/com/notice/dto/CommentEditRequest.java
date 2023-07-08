package com.notice.dto;

import lombok.Data;

@Data
public class CommentEditRequest {

	private Long commentId;
	
	private String content;
	
	private Long memberId;
}
