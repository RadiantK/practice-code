package com.notice.repository;

import java.util.List;
import java.util.Optional;

import com.notice.domain.Comment;
import com.notice.dto.CommentEditRequest;

public interface CommentRepository {

	List<Comment> findByNoticeId(Long noticeId);
	
	Optional<Comment> findById(Long commentId);
	
	void save(Comment comment);
	
	void edit(CommentEditRequest request);
	
	void remove(Long CommentId);
}
