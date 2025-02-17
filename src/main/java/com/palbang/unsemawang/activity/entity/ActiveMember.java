package com.palbang.unsemawang.activity.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.palbang.unsemawang.activity.constant.ActiveStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "activity_member", timeToLive = 10800)
public class ActiveMember implements Serializable {
	@Id
	private String memberId;

	private ActiveStatus status;

	private Long chatRoomId;

	@Builder.Default
	private boolean isSaved = false;

	@Builder.Default
	private LocalDateTime lastActiveAt = LocalDateTime.now();

	public void markAsSaved() {
		this.isSaved = true;
	}

}
