package com.palbang.unsemawang.activity.dto.websocket;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotifyActiveStatusMessage {

	private boolean isActive;

	public static NotifyActiveStatusMessage of(boolean isActive) {
		return NotifyActiveStatusMessage.builder()
			.isActive(isActive)
			.build();
	}
}
