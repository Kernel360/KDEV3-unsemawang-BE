package com.palbang.unsemawang.member.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupExtraInfoRequest {

	@JsonIgnore
	String id;

	@Schema(description = "닉네임")
	@NotBlank
	@Pattern(
		regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
		message = "닉네임은 2~10자의 한글, 영어, 숫자만 입력 가능합니다."
	)
	private String nickname;

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

	/* 검증 */
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

	@AssertTrue(message = "올바른 생년월일을 입력해주세요")
	private boolean isValidBirthday() {
		LocalDate currentDate = LocalDate.now();
		LocalDate birthday = LocalDate.of(year, month, day);

		if (currentDate.isBefore(birthday)) {
			return false;
		}

		return true;
	}

	public void updateMemberId(String id) {
		this.id = id;
	}
}
