package com.palbang.unsemawang.community.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;

	@Transactional(rollbackFor = Exception.class)
	public PostRegisterResponse register(PostRegisterRequest postRegisterRequest) {

		// 1. 게시글 등록
		Post post = Post.from(postRegisterRequest);
		Post savedPost = postRepository.save(post);

		// 2. 저장이 안됐다면 예외 발생
		if (savedPost.getId() == null) {
			throw new GeneralException(ResponseCode.ERROR_INSERT, "게시글 등록에 실패했습니다");
		}

		return PostRegisterResponse.of("게시글 등록이 성공했습니다!");
	}

}
