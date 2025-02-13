package com.palbang.unsemawang.chat.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 채팅방 ID

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user1_id", nullable = false)
	private Member user1;  // 작은 ID를 가진 사용자

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user2_id", nullable = false)
	private Member user2;  // 큰 ID를 가진 사용자

	@Column(nullable = false)
	private LocalDateTime registeredAt;  // 채팅방 생성 시간

	@Column(nullable = false)
	@Builder.Default
	private boolean active = true;  // 채팅방 활성 상태 기본값 true

	@Column(nullable = false)
	@Builder.Default
	private boolean isDelete = false;  // 채팅방 삭제 여부 기본값 false

	@Column(nullable = false)
	@Builder.Default
	private int unreadCount = 0;  // 안 읽은 메시지 개수 기본값 0

	// **두 사용자 ID를 정렬하여 채팅방을 생성**
	public static ChatRoom createSortedChatRoom(Member userA, Member userB) {
		if (userA.getId().compareTo(userB.getId()) < 0) {
			return ChatRoom.builder()
				.user1(userA)
				.user2(userB)
				.active(true)
				.isDelete(false)
				.registeredAt(LocalDateTime.now())
				.unreadCount(0)
				.build();
		} else {
			return ChatRoom.builder()
				.user1(userB)
				.user2(userA)
				.active(true)
				.isDelete(false)
				.registeredAt(LocalDateTime.now())
				.unreadCount(0)
				.build();
		}
	}

	public Optional<Member> getPartnerMember(String memberId) {
		if (memberId == null) {
			log.warn("getPartnerMember(String memberId) -> memberId가 null 임");
			return Optional.empty();
		}

		return Stream.of(user1, user2)
			.filter(Objects::nonNull) // null 인지 아닌지 확인
			.filter(m -> !m.getId().equals(memberId)) // memberId와 일치하지 않는 user 찾기
			.findFirst(); // 그 중 첫번째 객체 반환
	}
}
