package com.palbang.unsemawang.community.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.dto.response.CommentReadResponse;
import com.palbang.unsemawang.community.entity.Comment;
import com.palbang.unsemawang.community.repository.CommentRepository;
import com.palbang.unsemawang.community.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;

	@Transactional(readOnly = true)
	public LongCursorResponse<CommentReadResponse> getAllCommentsByPostId(Long postId,
		CursorRequest<Long> cursorRequest) {
		// 게시글 존재 확인
		postRepository.findById(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_POST));

		// 커서 키와 사이즈를 사용해 부모 댓글 페이징 조회
		LongCursorResponse<Comment> cursorResponse = commentRepository.findCommentsByPostIdAndCursor(
			postId,
			cursorRequest.key(),
			cursorRequest.size()
		);
		// 댓글 DTO 변환
		List<CommentReadResponse> commentResponseDtoList = cursorResponse.data().stream()
			.map(this::convertToReadResponse)
			.collect(Collectors.toList());

		return LongCursorResponse.of(cursorResponse.nextCursorRequest(), commentResponseDtoList);
	}

	// 댓글 및 대댓글을 DTO로 변환
	private CommentReadResponse convertToReadResponse(Comment comment) {
		// 자식 댓글 처리(자식 댓글 리스트 생성)
		List<CommentReadResponse> replies = comment.getChildComments().stream()
			.map(this::convertToReadResponse)
			.collect(Collectors.toList());

		return CommentReadResponse.builder()
			.commentId(comment.getId())
			.memberId(comment.getMember().getId().toString())
			.nickname(comment.getMember().getNickname())
			.isAnonymous(comment.getIsAnonymous())
			.content(comment.getContent())
			.registeredAt(comment.getRegisteredAt())
			.replies(replies) // 자식 댓글 없으면 null
			.repliesCount(replies.size()) // 자식 댓글 수
			.build();
	}
}
