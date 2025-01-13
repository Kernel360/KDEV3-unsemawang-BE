package com.palbang.unsemawang.fortune.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class UpdateResponse {
	@Schema(required = true)
	private String relationName;

	@Schema(required = true)
	private String nickname;

	@Schema(required = true)
	private int year;

	@Schema(required = true)
	private int month;

	@Schema(required = true)
	private int day;

	@Schema(required = true)
	private int hour;

	@Schema(required = true)
	private char sex;

	@Schema(required = true)
	private int youn;

	@Schema(required = true)
	private String solunar;
}
