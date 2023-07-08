package com.notice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.notice.domain.AttachFile;
import com.notice.domain.Notice;
import com.notice.dto.NoticeDetailResponse;
import com.notice.dto.NoticeEditDto;
import com.notice.dto.NoticeEditRequest;
import com.notice.dto.NoticeListParam;
import com.notice.dto.NoticeListRequest;
import com.notice.dto.NoticeRegistRequest;
import com.notice.dto.NoticeRemoveRequest;
import com.notice.dto.NoticeResponse;
import com.notice.exception.NoticeNotFoundException;
import com.notice.repository.AttachFileRepository;
import com.notice.repository.NoticeRepository;
import com.notice.util.Constant;
import com.notice.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeRepository noticeRepository;
	private final AttachFileRepository attachFileRepository;
	private final FileUtils fileUtils;
	
	// 전체 게시글 출력 
	public List<NoticeResponse> noticeList(NoticeListRequest request) {
		int page = (request.getPage() - 1) * Constant.PAGE_LIMIT_SIZE;
		
		// 이스케이프 문자 처리
		if (request.getWord().equals("%")) request.setWord("\\%");
		if (request.getWord().equals("\n")) request.setWord("\\n");
		
		NoticeListParam param = new NoticeListParam(page , request.getType(), request.getWord());
		
		// 게시글 lambda를 통한 변환 후 리턴
		return noticeRepository.findAll(param);
	}
	
	
	//게시글 총 수
	public int noticeCount(NoticeListRequest request) {
		return noticeRepository.countQuery(request);
	}
	
	// 게시글 등록
	public Long registNotice(NoticeRegistRequest request, Long memberId) {
		
		try {
			// 게시글 저장
			Notice notice = new Notice(
					request.getTitle(), 
					request.getContent(), 
					memberId);
			
			noticeRepository.save(notice);
			
			log.info("getNoticeId()={}", notice.getNoticeId());
			
			// 첨부파일 저장
			List<AttachFile> saveFiles = new ArrayList<>();
			saveFiles = fileUtils.saveFiles(request.getAttachFiles());

			// 첨부파일 db저장
			if (saveFiles != null && !saveFiles.isEmpty()) {
				for (AttachFile file : saveFiles) {
					// noticeId 설정
					file.setNoticeId(notice.getNoticeId());
					attachFileRepository.save(file);
				}
			}
			
			// 저장된 noticeId 리턴
			return notice.getNoticeId();
		} catch (IOException e) {
			throw new IllegalStateException("게시글 저장에 실패했습니다. ", e);
		}
		
	}
	
	// 게시글 상세 내용
	public NoticeDetailResponse detailNotice(Long id) {
		Notice findNotice = getNotice(id);
		
		NoticeDetailResponse detailNotice = noticeRepository.detailNotice(findNotice.getNoticeId());
		return detailNotice;
	}
	
	// 게시글 수정
	public void edit(NoticeEditRequest request) {
		Notice findNotice = getNotice(request.getNoticeId());
		
		try {
			noticeRepository.update(new NoticeEditDto(request.getNoticeId(), request.getTitle(), request.getContent()));
			
			// 첨부파일 서버 저장
			List<AttachFile> saveFiles = new ArrayList<>();
			
			saveFiles = fileUtils.saveFiles(request.getAttachFiles());
			
			// 첨부파일  db 저장
			if (saveFiles != null && !saveFiles.isEmpty()) {
				for (AttachFile file : saveFiles) {
					// noticeId 설정
					file.setNoticeId(findNotice.getNoticeId());
					attachFileRepository.save(file);
				}
			}
			
			// 체크박스에 체크된 첨부파일 제거
			List<Long> removeAttachIds = request.getRemoveAttachId();
			if (removeAttachIds != null && !removeAttachIds.isEmpty()) {
				for (Long id : removeAttachIds) {
					attachFileRepository.remove(id);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("파일 저장 실패!!", e);
		}
	}
	
	// 게시글 삭제
	public void removeNotice(NoticeRemoveRequest request) {
		Notice findNotice = getNotice(request.getNoticeId());
		log.info("findNotice : {}", findNotice.getNoticeId());
		
		if (!findNotice.getMemberId().equals(request.getMemberId())) {
			throw new IllegalArgumentException("자신의 게시물이 아닙니다.");
		}
	
		List<AttachFile> attachList = attachFileRepository.findByNoticeId(findNotice.getNoticeId());
		
		log.info("attachList : {}", attachList);
		
		noticeRepository.removeAll(findNotice.getNoticeId());
		
//		// 게시글에 대해 첨부파일이 존재하면
//		if (!attachList.isEmpty()) {
//			// db 첨부파일, 게시물 제거
//			
//			
//			// 서버에 저장된 파일 제거
//			for (AttachFile a : attachList) {
//				String fileName = fileUtils.getFullPathFileName(a.getSaveFileName());
//				boolean delete = new File(fileName).delete();
//				
//				if (!delete) log.info("서버에 파일이 존재지 않습니다.");
//			}
//			
//		} else {
//			// 첨부파일이 존재하지 않으면 게시물만 제거
//			noticeRepository.remove(findNotice.getNoticeId());
//		}
	}
	
	// 번호에 해당하 게시글 찾기
	private Notice getNotice(Long id) {
		return noticeRepository.findById(id)
				.orElseThrow(() -> new NoticeNotFoundException("게시글을 찾을 수 없습니다."));
	}
	
}
