package com.palbang.unsemawang.community.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.MyCommentsReadResponse;
import com.palbang.unsemawang.community.dto.response.PostListResponse;
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

		return postRepository.findMyPostsByCursor(memberId, cursorRequest, category, sort);
	}
}
