package com.notice.repository.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.notice.domain.AttachFile;

@Mapper
public interface AttachFileMapper {

	void save(AttachFile attachFile);
	
	Optional<AttachFile> findById(Long atachFileId);
	
	public List<AttachFile> findByNoticeId(Long noticeId);
	
	void remove(Long attachFileId);
	
}
