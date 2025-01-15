package com.palbang.unsemawang.member.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupExtraInfoRequest {

	@Schema(description = "회원 ID(쿠키 문제 해결 후 사라질 예정)")
	@NotBlank(message = "아이디를 입력해 주세요")
	private String id;

	@Schema(description = "닉네임")
	@NotBlank
	@Pattern(
		regexp = "^[가-힣a-zA-Z0-9_]{2,15}$",
		message = "닉네임은 2~15자의 한글, 영어, 숫자, '_'만 입력 가능합니다."
	)
	private String nickname;

	@Schema(description = "휴대폰 번호")
	@NotBlank(message = "휴대폰 번호를 입력해 주세요")
	@Pattern(
		regexp = "^01[0-9]-\\d{4}-\\d{4}$",
		message = "유효하지 않은 휴대폰 번호 형식 입니다. ex) 010-1234-1234"
	)
	private String phone;

	@Schema(description = "성별", defaultValue = "남", requiredProperties = {"남", "여"})
	@NotNull(message = "성별을 입력해 주세요")
	private Character sex;

	@Schema(description = "태어난 년도")
	@NotNull(message = "태어난 년도를 입력해 주세요")
	@Min(value = 1000, message = "년도는 1000 미만이 될 수 없습니다")
	private Integer year;

	@Schema(description = "태어난 월", defaultValue = "1")
	@NotNull(message = "태어난 월을 입력해 주세요")
	private Integer month;

	@Schema(description = "태어난 일", defaultValue = "1")
	@NotNull(message = "태어난 일을 입력해 주세요")
	private Integer day;

	@Schema(
		description = "태어난 시간",
		requiredProperties = {"0", "1", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22"}
	)
	@NotNull(message = "생시를 입력해 주세요")
	private Integer hour;

	@Schema(description = "양/음력")
	@NotBlank(message = "양/음력을 입력해 주세요")
	@Pattern(
		regexp = "^solar|lunar$",
		message = "양력(solar) 또는 음력(lunar)만 입력 가능 합니다"
	)
	private String solunar; // "solar", "lunar"

	@Schema(description = "평/윤달")
	@NotNull(message = "평/윤달을 입력해 주세요")
	@Min(value = 0, message = "평달(1) 또는 윤달(0)을 입력해 주세요")
	@Max(value = 1, message = "평달(1) 또는 윤달(0)을 입력해 주세요")
	private Integer youn; //윤달여부?

	@AssertTrue(message = "생시는 반드시 다음 숫자만 입력되어야 합니다: 0, 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22")
	private boolean isValidHour() {
		return List.of(0, 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22).contains(hour);
	}

	@AssertTrue(message = "태어난 월 또는 일수를 확인해 주세요")
	private boolean isValidDay() {
		if (month < 1 || month > 12) {
			return false;
		}

		// 월별 최대 일수를 배열로 정의
		int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		// 윤년일 경우 2월의 최대 일수를 29일로 변경
		if (this.month == 2 && (((this.year) % 4 == 0 && (this.year) % 100 != 0) || this.year % 400 == 0)) {
			daysInMonth[1] = 29;
		}

		// 일수가 유효한 범위에 있는지 확인
		return this.day >= 1 && this.day <= daysInMonth[this.month - 1];
	}

}
