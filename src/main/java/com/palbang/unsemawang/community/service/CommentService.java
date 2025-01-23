package com.palbang.unsemawang.community.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.dto.request.CommentRegisterRequest;
import com.palbang.unsemawang.community.dto.request.CommentUpdateRequest;
import com.palbang.unsemawang.community.dto.response.CommentReadResponse;
import com.palbang.unsemawang.community.entity.Comment;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.CommentRepository;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

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

	public void registerComment(Long postId, CommentRegisterRequest request, String memberId) {
		// 회원 존재 확인
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		// 게시글 존재 확인
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_POST));

		Comment parentComment = null;
		// 자식 댓글일 경우
		if (request.getParentCommentId() != null) {
			// 부모 댓글 존재 확인
			parentComment = commentRepository.findById(request.getParentCommentId())
				.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_COMMENT));

			// 대대댓글 방지 검증 로직
			if (parentComment.getParentComment() != null) {
				throw new GeneralException(ResponseCode.NOT_ALLOWED_NESTED_COMMENT);
			}
		}

		// 댓글 생성
		Comment comment = Comment.builder()
			.post(post)
			.parentComment(parentComment)
			.content(request.getContent())
			.isAnonymous(request.getIsAnonymous())
			.member(member) // 로그인된 회원 정보 연결
			.build();

		// 댓글 저장
		commentRepository.save(comment);
	}

	public void updateComment(Long postId, Long commentUd, CommentUpdateRequest request, String memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		postRepository.findByIdAndIsDeletedFalse(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_POST));

		Comment comment = commentRepository.findById(commentUd)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_COMMENT));

		// 로그인한 사용자와 댓글 작성자가 일치하는지 확인
		if (!Objects.equals(comment.getMember().getId(), member.getId())) {
			throw new GeneralException(ResponseCode.NOT_AUTHORIZED_COMMENT_MODIFICATION);
		}

		comment.updateComment(request.getContent(), request.getIsAnonymous()); // dirty-check -> save() 필요없음
	}
}