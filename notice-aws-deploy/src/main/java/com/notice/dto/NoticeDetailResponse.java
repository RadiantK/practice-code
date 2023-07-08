package com.notice.dto;

import java.util.ArrayList;
import java.util.List;

import com.notice.domain.AttachFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoticeDetailResponse {

	private Long noticeId;
	
	private String title;
	
	private String content;
	
	private String createdAt;
	
	private String updatedAt;
	
	private Long memberId;
	
	List<AttachFile> attachFiles = new ArrayList<>();

}
