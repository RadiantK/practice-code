package com.notice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.notice.domain.Comment;
import com.notice.dto.CommentEditRequest;
import com.notice.dto.CommentListRequest;
import com.notice.dto.CommentListResponse;
import com.notice.dto.CommentRegistRequest;
import com.notice.dto.CommentRemoveRequest;
import com.notice.exception.MemberNotFoundException;
import com.notice.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	
	public void addComment(CommentRegistRequest request) {
		Comment comment = CommentRegistRequest.toEntity(request);
		
		commentRepository.save(comment);
	}
	
	public List<CommentListResponse> commentList(CommentListRequest request) {
		List<Comment> findComments = commentRepository.findByNoticeId(request.getNoticeId());
		
		return findComments.stream()
				.map(c -> new CommentListResponse(
						c.getCommentId(), 
						c.getContent(), 
						c.getMemberId()))
				.collect(Collectors.toList());
	}
	
	public void removeComment(CommentRemoveRequest request) {
		commentRepository.remove(request.getCommentId());
	}
	
	public void editComment(CommentEditRequest request) {
		Comment findComment = commentRepository.findById(request.getCommentId())
		.orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
		
		if (!findComment.getMemberId().equals(request.getMemberId())) {
			throw new MemberNotFoundException("자신의 댓글이 아닙니다.");
		}
		
		commentRepository.edit(request);
		log.info("수정 완료");
	}
}
