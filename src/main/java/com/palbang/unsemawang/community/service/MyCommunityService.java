package com.palbang.unsemawang.community.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.MyCommentsReadResponse;
import com.palbang.unsemawang.community.dto.response.PostListResponse;
import com.palbang.unsemawang.community.dto.response.PostProjectionDto;
import com.palbang.unsemawang.community.repository.CommentRepository;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyCommunityService {
	private final MemberRepository memberRepository;
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final FileService fileService;

	@Transactional(readOnly = true)
	public LongCursorResponse<MyCommentsReadResponse> commentListRead(CursorRequest<Long> cursorRequest,
		String memberId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		return commentRepository.findMyCommentsByCursor(memberId, cursorRequest);
	}

	@Transactional(readOnly = true)
	public LongCursorResponse<PostListResponse> postListRead(CursorRequest<Long> cursorRequest,
		String memberId, CommunityCategory category, Sortingtype sort) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		// QueryDSL에서 PostProjectionDto 리스트 가져오기
		LongCursorResponse<PostProjectionDto> postResponse = postRepository.findMyPostsByCursor(memberId, cursorRequest,
			category, sort);

		// PostProjectionDto → PostListResponse 변환 및 fileService 호출
		List<PostListResponse> postList = postResponse.data().stream()
			.map(post -> convertToPostListResponse(post, memberId))
			.collect(Collectors.toList());

		return LongCursorResponse.of(postResponse.nextCursorRequest(), postList);
	}

	/**
	 * PostProjectionDto → PostListResponse 변환 메서드
	 */
	private PostListResponse convertToPostListResponse(PostProjectionDto post, String memberId) {
		return PostListResponse.builder()
			.cursorId(post.getId())
			.id(post.getId())
			.title(post.getTitle())
			.snippet(post.getSnippet())
			.viewCount(post.getViewCount())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.nickname(post.getNickname())
			.profileImageUrl(fileService.getProfileImgUrl(memberId)) // 프로필 이미지 가져오기
			.communityCategory(post.getCommunityCategory())
			.registeredAt(post.getRegisteredAt())
			.updatedAt(post.getUpdatedAt())
			.imageUrl(fileService.getPostThumbnailImgUrl(post.getId())) // 대표 이미지 가져오기
			.build();
	}
}
