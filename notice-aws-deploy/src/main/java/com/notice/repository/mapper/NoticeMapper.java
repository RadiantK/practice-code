package com.notice.repository.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.notice.domain.Notice;
import com.notice.dto.NoticeDetailResponse;
import com.notice.dto.NoticeEditDto;
import com.notice.dto.NoticeListParam;
import com.notice.dto.NoticeListRequest;
import com.notice.dto.NoticeResponse;

@Mapper
public interface NoticeMapper {

	void save(Notice notice);
	
	Optional<Notice> findById(Long id);
	
	List<NoticeResponse> findAll(NoticeListParam request);
	
	int countQuery(NoticeListRequest request);
	
	void update(NoticeEditDto request);
	
	void remove(Long noticeId);
	
	void removeAll(Long noticeId);
	
	NoticeDetailResponse detailNotice(Long noticeId);
}
