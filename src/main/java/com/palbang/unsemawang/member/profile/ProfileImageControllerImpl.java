package com.palbang.unsemawang.member.profile;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.common.util.file.dto.FileReferenceType;
import com.palbang.unsemawang.common.util.file.dto.FileRequest;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ProfileImageControllerImpl implements ProfileImageController {

	private final FileService fileService;

	@Override
	@PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> uploadProfileImage(@AuthenticationPrincipal CustomOAuth2User user,
		@RequestParam("file") MultipartFile file) {

		fileService.uploadImage(file, FileRequest.of(FileReferenceType.MEMBER_PROFILE_IMG, user.getId()));

		return ResponseEntity.ok().build();
	}

	@Override
	@PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> updateProfileImage(@AuthenticationPrincipal CustomOAuth2User user,
		@RequestParam("file") MultipartFile file) {

		fileService.updateImage(file, FileRequest.of(FileReferenceType.MEMBER_PROFILE_IMG, user.getId()));

		return ResponseEntity.ok().build();
	}

	@Override
	@DeleteMapping("/profile-image")
	public ResponseEntity<Void> deleteProfileImage(@AuthenticationPrincipal CustomOAuth2User user) {

		fileService.deleteFile(FileRequest.of(FileReferenceType.MEMBER_PROFILE_IMG, user.getId()));

		return ResponseEntity.ok().build();
	}

	@Override
	@GetMapping("/profile-image")
	public ResponseEntity<String> getProfileImage(@AuthenticationPrincipal CustomOAuth2User user) {

		return ResponseEntity.ok(fileService.getProfileImgUrl(user.getId()));
	}
}
