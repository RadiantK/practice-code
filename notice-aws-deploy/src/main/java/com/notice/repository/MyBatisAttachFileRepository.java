package com.notice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.notice.domain.AttachFile;
import com.notice.repository.mapper.AttachFileMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyBatisAttachFileRepository implements AttachFileRepository {

	private final AttachFileMapper attachFileMapper;
	
	@Override
	public void save(AttachFile attachFile) {
		attachFileMapper.save(attachFile);
	}
	
	@Override
	public Optional<AttachFile> findById(Long atachFileId) {
		return attachFileMapper.findById(atachFileId);
	}

	@Override
	public List<AttachFile> findByNoticeId(Long noticeId) {
		return attachFileMapper.findByNoticeId(noticeId);
	}

	@Override
	public void remove(Long attachFileId) {
		attachFileMapper.remove(attachFileId);
	}

}
