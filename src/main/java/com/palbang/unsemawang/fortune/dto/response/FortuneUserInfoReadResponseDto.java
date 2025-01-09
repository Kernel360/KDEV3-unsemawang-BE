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
public class FortuneUserInfoReadResponseDto {
	private String relationName;
	private String name;
	private char sex; // '남', '여'
	private LocalDate birthdate;
	private int birthtime;
	private String solunar; // "solar", "lunar"
	private int youn;
}