package com.palbang.unsemawang.community.service;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.community.dto.response.CommentReadResponse;
import com.palbang.unsemawang.community.entity.Comment;
import com.palbang.unsemawang.community.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;

	@Transactional(readOnly = true)
	public Page<CommentReadResponse> getCommentsByPostId(Long postId, Pageable pageable) {
		return commentRepository.findByPostIdAndParentCommentIsNull(postId, pageable)
			.map(this::mapToCommentReadResponse);
	}

	private CommentReadResponse mapToCommentReadResponse(Comment comment) {
		return CommentReadResponse.builder()
			.commentId(comment.getId())
			.memberId(comment.getMember().getId().toString())
			.nickname(comment.getMember().getNickname())
			.isAnonymous(comment.getIsAnonymous())
			.content(comment.getContent())
			.registeredAt(comment.getRegisteredAt())
			.replies(comment.getChildComments().stream()
				.map(this::mapToCommentReadResponse)
				.collect(Collectors.toList()))
			.repliesCount(comment.getChildComments().size())
			.build();
	}
}
