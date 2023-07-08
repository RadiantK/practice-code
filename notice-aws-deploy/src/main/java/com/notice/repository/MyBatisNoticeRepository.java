package com.notice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.notice.domain.Notice;
import com.notice.dto.NoticeDetailResponse;
import com.notice.dto.NoticeEditDto;
import com.notice.dto.NoticeListParam;
import com.notice.dto.NoticeListRequest;
import com.notice.dto.NoticeResponse;
import com.notice.repository.mapper.NoticeMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyBatisNoticeRepository implements NoticeRepository {

	private final NoticeMapper noticeMapper;
	
	@Override
	public void save(Notice notice) {
		noticeMapper.save(notice);
	}

	@Override
	public void update(NoticeEditDto request) {
		noticeMapper.update(request);
	}

	@Override
	public List<NoticeResponse> findAll(NoticeListParam request) {
		return noticeMapper.findAll(request);
	}

	@Override
	public int countQuery(NoticeListRequest request) {
		return noticeMapper.countQuery(request);
	}
	
	@Override
	public Optional<Notice> findById(Long id) {
		return noticeMapper.findById(id);
	}

	@Override
	public void remove(Long id) {
		noticeMapper.remove(id);
	}
	
	@Override
	public void removeAll(Long noticeId) {
		noticeMapper.removeAll(noticeId);
	}

	@Override
	public NoticeDetailResponse detailNotice(Long noticeId) {
		return noticeMapper.detailNotice(noticeId);
	}

}
