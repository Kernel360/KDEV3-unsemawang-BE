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
@JsonIgnoreProperties
public class UpdateResponse {
	private String relationName;
	private String nickname;
	private int year;
	private int month;
	private int day;
	private int birthtime;
	private char sex;
	private int youn;
	private String solunar;
}
