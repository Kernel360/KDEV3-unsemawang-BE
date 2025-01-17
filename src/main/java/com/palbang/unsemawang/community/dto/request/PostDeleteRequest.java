package com.palbang.unsemawang.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDeleteRequest {

	private Long postId;

	private String memberId;

	public static PostDeleteRequest of(Long postId, String memberId) {
		return new PostDeleteRequest(postId, memberId);
	}
}
