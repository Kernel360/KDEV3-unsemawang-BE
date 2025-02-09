package com.palbang.unsemawang.chat.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChatParticipantId implements Serializable {
	private Long chatRoomId;
	private Long participantId;
}
