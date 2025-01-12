package com.palbang.unsemawang.fortune.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

// 미사용 객체. 삭제 예정
public class FortuneApiResponse<T> {

	private Boolean success;
	private Integer code;
	private String message;
	private T data;
}