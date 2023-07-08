package com.notice.repository.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.notice.domain.Comment;
import com.notice.dto.CommentEditRequest;

@Mapper
public interface CommentMapper {

	List<Comment> findByNoticeId(Long noticeId);
	
	Optional<Comment> findById(Long commentId);
	
	void save(Comment comment);
	
	void edit(CommentEditRequest request);
	
	void remove(Long CommentId);
}
