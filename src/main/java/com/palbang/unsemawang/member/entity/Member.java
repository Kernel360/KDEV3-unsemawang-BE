package com.palbang.unsemawang.member.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.fortune.entity.FortuneCategory;
import com.palbang.unsemawang.member.constant.MemberRole;
import com.palbang.unsemawang.member.constant.MemberStatus;
import com.palbang.unsemawang.member.constant.OauthProvider;
import com.palbang.unsemawang.member.dto.SignupExtraInfoRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "member")
public class Member extends BaseEntity {

	// 공통 컬럼
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "name")
	private String name;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "birth_date")
	private LocalDate birthdate;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "profile_url")
	private String profileUrl;

	@Column(name = "point")
	private int point;

	@Column(name = "gender")
	private char gender;  // 성별 (F, M)

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;  // 최근접속일자

	@Column(name = "changed_at")
	private LocalDateTime changedAt; //변경일자

	@Enumerated(EnumType.STRING)
	@Column(name = "memberStatus")
	public MemberStatus memberStatus; //계정상태

	private Boolean isDeleted;

	private LocalDateTime deletedAt;

	private Boolean isTermsAgreed; // 약관 동의 true/false

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites; // 사용자가 찜한 목록

	// 일반 회원&관리자 전용 컬럼
	@Column(name = "oauth_id", nullable = false)
	private String oauthId; //oauth에게 제공받은 id

	@Column(name = "oauth_provider")
	@Enumerated(EnumType.STRING)
	private OauthProvider oauthProvider;

	@Column
	private Boolean isJoin; // OAuth 로그인 후 추가 입력까지 완료한 상태인지

	// 전문가 전용 컬럼
	private String password;

	private String address; // 전문가 사업장 주소

	private String career;

	private String shortBio; // 한줄소개

	private String detailBio; // 상세소개

	@ManyToOne(fetch = FetchType.LAZY)
	private FortuneCategory fortuneCategory;

	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favoritedBy; // 사용자가 찜받은 목록

	public void updateExtraInfo(SignupExtraInfoRequest signupExtraInfoDto) {
		this.phoneNumber = signupExtraInfoDto.getPhone();
		this.nickname = signupExtraInfoDto.getNickname();
		this.changedAt = LocalDateTime.now();
	}
}
