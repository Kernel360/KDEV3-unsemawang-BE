package com.palbang.unsemawang.fortune.dto.response;

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
public class FortuneInfoRegisterResponseDto {
	private String memberId;
	private String relationName;
	private String name;
	private char sex; // '남', '여'
	private int year;
	private int month;
	private int day;
	private int birthtime;
	private String solunar; // "solar", "lunar"
	private int youn;
}