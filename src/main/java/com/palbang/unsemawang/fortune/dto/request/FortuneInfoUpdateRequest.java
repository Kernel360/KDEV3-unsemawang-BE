package com.palbang.unsemawang.fortune.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class FortuneInfoUpdateRequest {
	@Schema(required = false)
	private String memberId;

	@Schema(required = true)
	@NotNull(message = "relationId는 필수 값입니다.")
	private Long relationId;

	@Schema(required = true)
	@NotBlank(message = "relationName은 필수 값입니다.")
	private String relationName;

	@Schema(required = true)
	@NotBlank(message = "nickname은 필수 값입니다.")
	@JsonProperty("name")
	private String nickname;

	@Schema(required = true)
	@NotNull(message = "year는 필수 값입니다.")
	private int year;

	@Schema(required = true)
	@NotNull(message = "month는 필수 값입니다.")
	private int month;

	@Schema(required = true)
	@NotNull(message = "day는 필수 값입니다.")
	private int day;

	@Schema(required = true)
	private int hour; // 태어난 시간 (null 허용)

	@Schema(required = true)
	@NotNull(message = "sex는 필수 값입니다.")
	private char sex;

	@Schema(required = true)
	@NotNull(message = "youn은 필수 값입니다.")
	private int youn;

	@Schema(required = true)
	@NotBlank(message = "solunar는 필수 값입니다.")
	@Pattern(regexp = "^(solar|lunar)$", message = "solunar는 'solar' 또는 'lunar'만 가능합니다.")
	private String solunar;
}
