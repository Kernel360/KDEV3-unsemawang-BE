package com.palbang.unsemawang.activity.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

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

	@Builder.Default
	private LocalDateTime lastActiveDateTime = LocalDateTime.now();

	@TimeToLive
	private Long ttl;
}
