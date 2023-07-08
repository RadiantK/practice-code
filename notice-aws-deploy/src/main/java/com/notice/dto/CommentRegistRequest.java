package com.notice.dto;

import com.notice.domain.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRegistRequest {

	private String content;
	private Long noticeId;
	private Long memberId;
	
	public static Comment toEntity(CommentRegistRequest r) {
		return new Comment(
				r.getContent(),
				r.getNoticeId(),
				r.getMemberId());
	}
}
