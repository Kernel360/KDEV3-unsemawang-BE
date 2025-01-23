package com.palbang.unsemawang.fortune.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "궁합 요청 객체")
public class GunghapApiRequest {
	@Schema(description = "본인 정보", required = true)
	@Valid
	private FortuneApiRequest me;

	@Schema(description = "상대 정보", required = false)
	@Valid
	private FortuneApiRequest other;
}
