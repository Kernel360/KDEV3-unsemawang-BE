package com.palbang.unsemawang.chat.entity;

import java.time.LocalDateTime;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(ChatParticipantId.class)
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "chat_participant")
public class ChatParticipant extends BaseEntity {
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id")
	private ChatRoom chatRoom;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "participant_id")
	private Member participant;

	@Column(name = "is_exit", nullable = false)
	@Builder.Default
	private boolean isExit = false;

	@Column(name = "last_read_at")
	private LocalDateTime lastReadAt;
}
