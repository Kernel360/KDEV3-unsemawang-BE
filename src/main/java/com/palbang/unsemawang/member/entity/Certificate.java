package com.palbang.unsemawang.member.entity;

import java.time.LocalDate;

import com.palbang.unsemawang.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Certificate extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "expert_id")
	private Member expert;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String fileUrl;

	@Column(nullable = false)
	private String issuer; // 발급처

	@Column(nullable = false)
	private LocalDate issuedDate; // 발급날짜
}
