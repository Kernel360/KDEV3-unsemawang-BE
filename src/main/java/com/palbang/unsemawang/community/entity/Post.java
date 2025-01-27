package com.palbang.unsemawang.community.entity;

import java.time.LocalDateTime;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "post")
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "is_anonymous", nullable = false)
	@Builder.Default
	private Boolean isAnonymous = false;

	@Column(nullable = false)
	@Builder.Default
	private String title = "빈 제목";

	@Column(columnDefinition = "TEXT", nullable = false)
	@Builder.Default
	private String content = "빈 내용";

	@Column(name = "view_count")
	@Builder.Default
	private Integer viewCount = 0;

	@Column(name = "like_count")
	@Builder.Default
	private Integer likeCount = 0;

	@Column(name = "comment_count")
	@Builder.Default
	private Integer commentCount = 0;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CommunityCategory communityCategory;

	@Column(name = "is_visible")
	@Builder.Default
	private Boolean isVisible = true;

	@Column(name = "is_deleted")
	@Builder.Default
	private Boolean isDeleted = false;

	@Column(name = "registered_at", updatable = false)
	@Builder.Default
	private LocalDateTime registeredAt = LocalDateTime.now();

	@Column(name = "updated_at")
	@Builder.Default
	private LocalDateTime updatedAt = LocalDateTime.now();

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public String getWriterId() {
		return this.member.getId();
	}

	public void deletePost() {
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
		this.isVisible = false;
	}

	public void updateMember(Member member) {
		this.member = member;
	}

	public void updateFrom(PostUpdateRequest postUpdateRequest) {
		this.title = postUpdateRequest.getTitle();
		this.content = postUpdateRequest.getContent();
		this.communityCategory = postUpdateRequest.getCategory();
		this.isAnonymous = postUpdateRequest.getCategory() == CommunityCategory.ANONYMOUS_BOARD;
		this.updatedAt = LocalDateTime.now();
	}

	public static Post from(PostRegisterRequest postRegisterRequest) {
		return Post.builder()
			.content(postRegisterRequest.getContent())
			.title(postRegisterRequest.getTitle())
			.isAnonymous(postRegisterRequest.getCategory() == CommunityCategory.ANONYMOUS_BOARD)
			.communityCategory(postRegisterRequest.getCategory())
			.build();
	}

	public void incrementCommentCount() {
		this.commentCount++;
		this.updatedAt = LocalDateTime.now();
	}

	public void decrementCommentCount(Integer decrementCount) {
		if (this.commentCount - decrementCount >= 0) {
			this.commentCount -= decrementCount;
		} else {
			this.commentCount = 0;
		}
		this.updatedAt = LocalDateTime.now();
	}
}
