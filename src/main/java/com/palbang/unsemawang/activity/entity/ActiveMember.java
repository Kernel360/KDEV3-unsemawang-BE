package com.palbang.unsemawang.activity.entity;

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
@RedisHash(value = "activity_member")
public class ActiveMember {
	@Id
	private String memberId;

	private ActiveStatus status;

	private Long chatRoomId;

	@Builder.Default
	private LocalDateTime lastActiveAt = LocalDateTime.now();

}
