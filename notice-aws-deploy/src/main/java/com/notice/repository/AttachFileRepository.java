package com.notice.repository;

import java.util.List;
import java.util.Optional;

import com.notice.domain.AttachFile;

public interface AttachFileRepository {

	void save(AttachFile attachFile);
	
	Optional<AttachFile> findById(Long ttachFileId);
	
	List<AttachFile> findByNoticeId(Long noticeId);
	
	void remove(Long attachFileId);
}
