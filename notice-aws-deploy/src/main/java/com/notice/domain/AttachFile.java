package com.notice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttachFile {

	private Long attachFileId;
	
	private String orgFileName;
	
	private String saveFileName;
	
	private String ext;
	
	private Long size;

	private Long noticeId;
	
	public AttachFile(String orgFileName, String saveFileName, String ext, Long size) {
		this.orgFileName = orgFileName;
		this.saveFileName = saveFileName;
		this.ext = ext;
		this.size = size;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	
}
