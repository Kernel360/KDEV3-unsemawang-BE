package com.palbang.unsemawang.fortune.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class FortuneInfoResponseDto {
	private String memberId;
	private String relationName;
	private String name;
	private char sex; // '남', '여'
	private LocalDate birthdate;
	private String birthtime;
	private String solunar; // "solar", "lunar"
	private int youn;
}