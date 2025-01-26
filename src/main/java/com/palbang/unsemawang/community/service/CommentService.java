package com.palbang.unsemawang.community.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.request.CommentRegisterRequest;
import com.palbang.unsemawang.community.dto.request.CommentUpdateRequest;
import com.palbang.unsemawang.community.dto.response.CommentReadResponse;
import com.palbang.unsemawang.community.entity.AnonymousMapping;
import com.palbang.unsemawang.community.entity.Comment;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.AnonymousMappingRepository;
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
	private final AnonymousMappingRepository anonymousMappingRepository;

	@Transactional(readOnly = true)
	public LongCursorResponse<CommentReadResponse> getAllCommentsByPostId(Long postId,
		CursorRequest<Long> cursorRequest) {
		// 게시글 존재 확인
		Post post = postRepository.findByIdAndIsDeletedFalse(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_POST));

		// 부모 댓글 페이징 조회
		LongCursorResponse<Comment> parentResponse = commentRepository.findCommentsByPostIdAndCursor(
			postId,
			cursorRequest.key(),
			cursorRequest.size()
		);

		// 부모 댓글이 없으면 빈 응답 반환
		if (parentResponse.data().isEmpty()) {
			return LongCursorResponse.empty(cursorRequest);
		}

		// 2. 부모 댓글 ID 리스트 추출
		List<Long> parentIds = parentResponse.data().stream()
			.map(Comment::getId)
			.collect(Collectors.toList());

		// 부모 댓글이 없으면 빈 응답 반환
		if (parentIds.isEmpty()) {
			return LongCursorResponse.of(cursorRequest, List.of());
		}

		// 3. 자식 댓글 조회
		List<Comment> childComments = commentRepository.findChildCommentsByParentIds(parentIds);

		// 4. 자식 댓글을 부모 댓글에 매핑
		mapChildCommentsToParent(parentResponse.data(), childComments);

		// DTO 변환
		List<CommentReadResponse> responseDtoList = parentResponse.data().stream()
			.map(comment -> convertToReadResponse(comment, post.getCommunityCategory()))
			.collect(Collectors.toList());

		return LongCursorResponse.of(parentResponse.nextCursorRequest(), responseDtoList);
	}

	// 자식 댓글을 부모 댓글에 매핑
	private void mapChildCommentsToParent(List<Comment> parentComments, List<Comment> childComments) {
		// 1. 자식 댓글을 부모 댓글 ID를 기준으로 그룹화
		Map<Long, List<Comment>> childCommentMap = childComments.stream()
			.collect(Collectors.groupingBy(child -> child.getParentComment().getId()));

		// 2. 각 부모 댓글에 해당하는 자식 댓글을 매핑
		parentComments.forEach(parent -> {
			List<Comment> children = childCommentMap.getOrDefault(parent.getId(), List.of());
			parent.getChildComments().clear(); // 기존 자식 댓글 초기화
			parent.getChildComments().addAll(children); // 새로운 자식 댓글 추가
		});
	}

	private CommentReadResponse convertToReadResponse(Comment comment, CommunityCategory category) {
		List<CommentReadResponse> replies = comment.getChildComments().stream()
			.map(child -> convertToReadResponse(child, category))
			.collect(Collectors.toList());

		// 게시판 카테고리에 따라 닉네임 결정
		String nickname = resolveNicknameForReadResponse(comment, category);

		return CommentReadResponse.builder()
			.commentId(comment.getId())
			.nickname(nickname)
			.content(comment.getContent())
			.registeredAt(comment.getRegisteredAt())
			.replies(replies)
			.repliesCount(replies.size())
			.build();
	}

	private String resolveNicknameForReadResponse(Comment comment, CommunityCategory category) {
		if (category == CommunityCategory.ANONYMOUS_BOARD) {
			// 익명 게시판일 경우 AnonymousMapping 테이블에서 익명 이름 가져오기
			return anonymousMappingRepository.findByPostIdAndMemberId(comment.getPost().getId(),
					comment.getMember().getId())
				.map(AnonymousMapping::getAnonymousName)
				.orElse("알 수 없음"); // 매핑이 없는 경우 기본값
		} else if (category == CommunityCategory.FREE_BOARD) {
			// 자유 게시판일 경우 사용자의 닉네임 반환
			return comment.getMember().getNickname();
		}
		throw new GeneralException(ResponseCode.INVALID_CATEGORY); // 유효하지 않은 카테고리 처리
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

		// 익명 게시판인 경우 익명 이름 생성
		String nickname = post.getCommunityCategory() == CommunityCategory.ANONYMOUS_BOARD
			? resolveAnonymousName(postId, memberId)
			: member.getNickname();

		// 댓글 생성
		Comment comment = Comment.builder()
			.post(post)
			.parentComment(parentComment)
			.content(request.getContent())
			.member(member) // 로그인된 회원 정보 연결
			.build();

		// 댓글 저장
		commentRepository.save(comment);

		// 게시글 댓글 수 증가
		post.incrementCommentCount();
		postRepository.save(post);
	}

	private String resolveAnonymousName(Long postId, String memberId) {
		return anonymousMappingRepository.findByPostIdAndMemberId(postId, memberId)
			.map(AnonymousMapping::getAnonymousName)
			.orElseGet(() -> {
				long anonymousIndex = anonymousMappingRepository.countByPostId(postId) + 1;
				String anonymousName = "익명" + anonymousIndex;

				AnonymousMapping mapping = AnonymousMapping.builder()
					.postId(postId)
					.memberId(memberId)
					.anonymousName(anonymousName)
					.build();
				anonymousMappingRepository.save(mapping);

				return anonymousName;
			});
	}

	public void updateComment(Long postId, Long commentId, CommentUpdateRequest request, String memberId) {
		Member member = validateMember(memberId);

		validatePost(postId);

		Comment comment = validateComment(commentId);

		validateCommentOwnerMatch(member.getId(), comment.getMember().getId());

		comment.updateComment(request.getContent()); // dirty-check -> save() 필요없음
	}

	public void deleteComment(Long postId, Long commentId, String memberId) {
		Member member = validateMember(memberId);

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_POST));

		Comment comment = validateComment(commentId);

		validateCommentOwnerMatch(member.getId(), comment.getMember().getId());

		comment.deleteComment();

		// 자식 댓글이 포함된 댓글 수 감소 처리
		int decrementCount = 1 + (int)comment.getChildComments().stream().filter(c -> !c.getIsDeleted()).count();
		post.decrementCommentCount(decrementCount);
		postRepository.save(post);
	}

	// 유효성 검증 메서드
	private Member validateMember(String memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));
	}

	private void validatePost(Long postId) {
		postRepository.findByIdAndIsDeletedFalse(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_POST));
	}

	private Comment validateComment(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_COMMENT));
	}

	// 로그인한 사용자와 댓글 작성자가 일치하는지 확인
	private void validateCommentOwnerMatch(String commentOwnerId, String memberId) {
		if (!Objects.equals(commentOwnerId, memberId)) {
			throw new GeneralException(ResponseCode.NOT_AUTHORIZED_COMMENT_MODIFICATION);
		}
	}
}