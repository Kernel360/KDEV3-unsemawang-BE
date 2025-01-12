package com.palbang.unsemawang.fortune.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "궁합 요청 객체")
public class GunghapApiRequest {
	@Schema(description = "본인 정보")
	private UserInfo me;

	@Schema(description = "상대 정보")
	private UserInfo other;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserInfo {

		@Schema(description = "사용자 이름", example = "이몽룡")
		@NotBlank(message = "name must not be empty")
		private String name;

		@Schema(description = "성별", example = "남")
		@Pattern(regexp = "남|여", message = "sex must be one of the following values: 남, 여")
		private String sex;

		@Schema(description = "출생 연도", example = "1990")
		@Min(value = 1, message = "year must not be less than 1")
		private int year;

		@Schema(description = "출생 월", example = "3")
		@Min(value = 1, message = "month must not be less than 1")
		@Max(value = 12, message = "month must not be greater than 12")
		private int month;

		@Schema(description = "출생 일", example = "15")
		@Min(value = 1, message = "day must not be less than 1")
		@Max(value = 31, message = "day must not be greater than 31")
		private int day;

		@Schema(description = "출생 시", example = "10")
		@Min(value = 0, message = "hour must not be less than 0")
		@Max(value = 12, message = "hour must not be greater than 12")
		private int hour;

		@Schema(description = "양력(solar)/음력(lunar)", example = "solar")
		@Pattern(regexp = "solar|lunar", message = "solunar must be one of the following values: solar, lunar")
		private String solunar;

		@Schema(description = "윤달(0)/평달(1)", example = "0")
		@Min(value = 0, message = "youn must not be less than 0")
		@Max(value = 1, message = "youn must not be greater than 1")
		private int youn;

	}
}
