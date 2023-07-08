package com.notice.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NoticeEditRequest {

	private Long noticeId;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String content;
	
	private List<MultipartFile> attachFiles = new ArrayList<>();
	
	private List<Long> removeAttachId = new ArrayList<>();

}
