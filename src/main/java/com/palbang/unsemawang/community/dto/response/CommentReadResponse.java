package com.palbang.unsemawang.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReadResponse {
	@Schema(description = "", required = true)
	private Long commentId;

	@Schema(description = "", required = true)
	private String memberId;

	@Schema(description = "", required = true)
	private String nickname;

	@Schema(description = "", required = true)
	private Boolean isAnonymous;

	@Schema(description = "", required = true)
	private String content;

	@Schema(description = "", required = true)
	private LocalDateTime registeredAt;

	@Schema(description = "", required = false)
	private List<CommentReadResponse> replies;

	@Schema(description = "", required = false)
	private int repliesCount;

}
