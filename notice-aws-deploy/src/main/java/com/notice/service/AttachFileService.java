package com.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.notice.domain.AttachFile;
import com.notice.repository.AttachFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachFileService {

	private final AttachFileRepository attachFileRepository;
	
	public List<AttachFile> test(Long id) {
		return attachFileRepository.findByNoticeId(id);
	}
}
