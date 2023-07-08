package com.notice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.notice.domain.Comment;
import com.notice.dto.CommentEditRequest;
import com.notice.repository.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyBatisCommentRepository implements CommentRepository {

	private final CommentMapper commentMapper;
	
	@Override
	public List<Comment> findByNoticeId(Long noticeId) {
		return commentMapper.findByNoticeId(noticeId);
	}

	@Override
	public void save(Comment comment) {
		commentMapper.save(comment);
	}

	@Override
	public void edit(CommentEditRequest request) {
		commentMapper.edit(request);
		
	}
	
	@Override
	public void remove(Long CommentId) {
		commentMapper.remove(CommentId);
	}

	@Override
	public Optional<Comment> findById(Long commentId) {
		return commentMapper.findById(commentId);
	}

}
