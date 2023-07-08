package com.notice.repository;

import java.util.List;
import java.util.Optional;

import com.notice.domain.Notice;
import com.notice.dto.NoticeDetailResponse;
import com.notice.dto.NoticeEditDto;
import com.notice.dto.NoticeListParam;
import com.notice.dto.NoticeListRequest;
import com.notice.dto.NoticeResponse;

public interface NoticeRepository {
	
	void save(Notice notice);
	
	void update(NoticeEditDto request);
	
	List<NoticeResponse> findAll(NoticeListParam request);
	
	int countQuery(NoticeListRequest request);
	
	Optional<Notice> findById(Long id);
	
	void remove(Long noticeId);
	
	void removeAll(Long noticeId);
	
	NoticeDetailResponse detailNotice(Long noticeId);
}
