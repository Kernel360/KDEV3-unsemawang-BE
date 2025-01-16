package com.palbang.unsemawang.community.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	@Transactional(rollbackFor = Exception.class)
	public PostRegisterResponse register(PostRegisterRequest postRegisterRequest) {

		// 0. 유효한 회원인지 확인
		Member member = memberRepository.findById(postRegisterRequest.getMemberId())
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "유효하지 않은 회원 ID 입니다"));

		// 1. 게시글 등록
		Post post = Post.from(postRegisterRequest);
		post.updateMember(member);
		Post savedPost = postRepository.save(post);

		// 2. 저장이 안됐다면 예외 발생
		if (savedPost.getId() == null) {
			throw new GeneralException(ResponseCode.ERROR_INSERT, "게시글 등록에 실패했습니다");
		}

		return PostRegisterResponse.of("게시글 등록이 성공했습니다!");
	}

	@Transactional(rollbackFor = Exception.class)
	public Void update(String memberId, PostUpdateRequest postUpdateRequest) {

		// 0. 유효한 회원인지 확인
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "유효하지 않은 회원 ID 입니다"));

		// 1. 회원 ID, 게시글 ID가 일치하는 게시글 조회
		Post post = postRepository.findByIdAndMember(postUpdateRequest.getPostId(), member)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "유효하지 않는 게시글 입니다"));

		// 2. 게시글 업데이트
		post.updateFrom(postUpdateRequest);

		return null;
	}

}
