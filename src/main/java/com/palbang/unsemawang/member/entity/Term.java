package com.palbang.unsemawang.member.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Term {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String version;

	private String description;

	private Boolean isRequired;

	private LocalDateTime registeredAt;

	private LocalDateTime updatedAt;

	private Boolean isDeleted;

	private Boolean isVisible;

	private LocalDateTime deletedAt;
}
