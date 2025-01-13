package com.palbang.unsemawang.community.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.palbang.unsemawang.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private Comment parentComment; // 부모 댓글 (Self-Referencing)

	@OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> childComments = new ArrayList<>(); // 대댓글 목록

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id", nullable = false)
	private Member memberId;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(name = "is_anonymous")
	@Builder.Default
	private Boolean isAnonymous = false;

	@Column(name = "is_visible")
	@Builder.Default
	private Boolean isVisible = true;

	@Column(name = "is_deleted")
	@Builder.Default
	private Boolean isDeleted = false;

	@Column(name = "registered_at")
	@Builder.Default
	private LocalDateTime registeredAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public void deleteComment() {
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
	}

}
