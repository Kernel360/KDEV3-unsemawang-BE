package com.palbang.unsemawang.community.entity;

import com.palbang.unsemawang.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "anonymous_mapping", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"post_id", "member_id"}) // 게시글별 member_id 유니크 보장
})
public class AnonymousMapping extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 고유 식별자

	@Column(name = "post_id", nullable = false)
	private Long postId; // 게시글 ID

	@Column(name = "member_id", nullable = false, length = 255)
	private String memberId; // 작성자 ID

	@Column(name = "anonymous_name", nullable = false, length = 50)
	private String anonymousName; // 익명 이름
}
