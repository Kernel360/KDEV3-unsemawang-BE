package com.palbang.unsemawang.community.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
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
	private final FileService fileService;

	@Transactional(readOnly = true)
	public LongCursorResponse<CommentReadResponse> getAllCommentsByPostId(Long postId,
		CursorRequest<Long> cursorRequest, String memberId) {
		// 게시글 존재 확인
		Post post = validatePost(postId);

		// 부모 댓글 페이징 조회
		LongCursorResponse<Comment> parentResponse = commentRepository.findCommentsByPostIdAndCursor(
			postId,
			cursorRequest.cursorKey(),
			cursorRequest.size()
		);

		// 부모 댓글이 없으면 빈 응답 반환
		if (parentResponse.data().isEmpty()) {
			return LongCursorResponse.empty(cursorRequest);
		}

		// 부모 댓글 ID 리스트 추출
		List<Long> parentIds = parentResponse.data().stream()
			.map(Comment::getId)
			.collect(Collectors.toList());

		// 자식 댓글 조회
		List<Comment> childComments = commentRepository.findChildCommentsByParentIds(parentIds);

		// 자식 댓글을 부모 댓글에 매핑
		mapChildCommentsToParent(parentResponse.data(), childComments);

		// DTO 변환
		List<CommentReadResponse> responseDtoList = parentResponse.data().stream()
			.map(comment -> convertToReadResponse(comment, post.getCommunityCategory(), memberId))
			.collect(Collectors.toList());

		return LongCursorResponse.of(parentResponse.nextCursorRequest(), responseDtoList);
	}

	// 자식 댓글을 부모 댓글에 매핑
	private void mapChildCommentsToParent(List<Comment> parentComments, List<Comment> childComments) {
		// 자식 댓글을 부모 댓글 ID를 기준으로 그룹화
		Map<Long, List<Comment>> childCommentMap = childComments.stream()
			.collect(Collectors.groupingBy(child -> child.getParentComment().getId()));

		// 각 부모 댓글에 해당하는 자식 댓글을 매핑
		parentComments.forEach(parent -> {
			List<Comment> children = childCommentMap.getOrDefault(parent.getId(), List.of());
			parent.getChildComments().clear(); // 기존 자식 댓글 초기화
			parent.getChildComments().addAll(children); // 새로운 자식 댓글 추가
		});
	}

	private CommentReadResponse convertToReadResponse(Comment comment, CommunityCategory category, String memberId) {
		List<CommentReadResponse> replies = comment.getChildComments().stream()
			.map(child -> convertToReadResponse(child, category, memberId))
			.collect(Collectors.toList());

		// 게시판 카테고리에 따라 닉네임 결정
		String nickname = resolveNicknameForReadResponse(comment, category);
		String imageUrl = resolveProfileImageForReadResponse(comment, category);

		return CommentReadResponse.builder()
			.commentId(comment.getId())
			.nickname(nickname)
			.content(comment.getContent())
			.registeredAt(comment.getRegisteredAt())
			.replies(replies)
			.repliesCount(replies.size())
			.imageUrl(imageUrl)
			.isMyComment(comment.getMember().getId().equals(memberId))
			.build();
	}

	private String resolveNicknameForReadResponse(Comment comment, CommunityCategory category) {
		if (category == CommunityCategory.ANONYMOUS_BOARD) {
			// 익명 게시판일 경우 AnonymousMapping 테이블에서 익명 이름 가져오기
			return anonymousMappingRepository.findByPostIdAndMemberId(comment.getPost().getId(),
					comment.getMember().getId())
				.map(AnonymousMapping::getAnonymousName)
				.orElse("알 수 없음"); // 매핑이 없는 경우 기본값
		} else {
			// 자유 게시판일 경우 사용자의 닉네임 반환
			return comment.getMember().getNickname();
		}
	}

	private String resolveProfileImageForReadResponse(Comment comment, CommunityCategory category) {
		if (category == CommunityCategory.ANONYMOUS_BOARD) {
			// 익명 게시판: 고정된 익명 이미지 반환
			return fileService.getAnonymousProfileImgUrl();
		} else if (category == CommunityCategory.FREE_BOARD) {
			// 자유 게시판: 사용자의 프로필 이미지 반환 (fileService 활용)
			return fileService.getProfileImgUrl(comment.getMember().getId());
		}
		throw new GeneralException(ResponseCode.ERROR_SEARCH);
	}

	public void registerComment(Long postId, CommentRegisterRequest request, String memberId) {
		// 회원 존재 확인
		Member member = validateMember(memberId);

		// 게시글 존재 확인
		Post post = validatePost(postId);

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

		// 익명 게시판인 경우 익명 이름 생성만 호출
		if (post.getCommunityCategory() == CommunityCategory.ANONYMOUS_BOARD) {
			resolveAnonymousName(postId, memberId); // 익명 이름 생성
		}

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

	private void resolveAnonymousName(Long postId, String memberId) {
		boolean exists = anonymousMappingRepository.findByPostIdAndMemberId(postId, memberId).isPresent();

		if (!exists) {
			long anonymousIndex = anonymousMappingRepository.countByPostId(postId) + 1;
			String anonymousName = "익명" + anonymousIndex;

			AnonymousMapping mapping = AnonymousMapping.builder()
				.postId(postId)
				.memberId(memberId)
				.anonymousName(anonymousName)
				.build();

			anonymousMappingRepository.save(mapping); // 익명 이름 저장
		}
	}

	public void updateComment(Long commentId, CommentUpdateRequest request, String memberId) {
		Comment comment = validateComment(commentId);

		// 소프트 삭제 된 댓글인지 확인
		if (comment.getIsDeleted()) {
			throw new GeneralException(ResponseCode.NOT_DELETE_AVAILABLE);
		}
		validatePost(comment.getPost().getId());

		Member member = validateMember(memberId);
		validateCommentOwnerMatch(member.getId(), comment.getMember().getId());

		comment.updateComment(request.getContent()); // dirty-check -> save() 필요없음
	}

	public void deleteComment(Long commentId, String memberId) {
		Comment comment = validateComment(commentId);
		Post post = comment.getPost();

		Member member = validateMember(memberId);
		validateCommentOwnerMatch(comment.getMember().getId(), member.getId());

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

	private Post validatePost(Long postId) {
		return postRepository.findByIdAndIsDeletedFalse(postId)
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