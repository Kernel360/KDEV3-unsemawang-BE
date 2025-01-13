package com.palbang.unsemawang.fortune.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
public class UpdateRequest {
	@NotBlank(message = "memberId는 필수 값입니다.")
	private String memberId;

	@NotNull(message = "relationId는 필수 값입니다.")
	private Long relationId;

	@NotBlank(message = "relationName은 필수 값입니다.")
	private String relationName;

	@NotBlank(message = "nickname은 필수 값입니다.")
	private String nickname;

	@NotNull(message = "year는 필수 값입니다.")
	private int year;

	@NotNull(message = "month는 필수 값입니다.")
	private int month;

	@NotNull(message = "day는 필수 값입니다.")
	private int day;

	private int birthtime; // 태어난 시간 (null 허용)

	@NotNull(message = "sex는 필수 값입니다.")
	private char sex;

	@NotNull(message = "youn은 필수 값입니다.")
	private int youn;

	@NotBlank(message = "solunar는 필수 값입니다.")
	private String solunar;
}
