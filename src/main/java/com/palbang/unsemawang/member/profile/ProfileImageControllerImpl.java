package com.palbang.unsemawang.member.profile;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.common.util.file.dto.FileReferenceType;
import com.palbang.unsemawang.common.util.file.dto.FileRequest;
import com.palbang.unsemawang.common.util.file.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ProfileImageControllerImpl implements ProfileImageController {

	private final FileService fileService;

	@Override
	@PostMapping(value = "/{memberId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> uploadProfileImage(@PathVariable String memberId,
		@RequestParam("file") MultipartFile file) {

		fileService.uploadImage(file, FileRequest.of(FileReferenceType.MEMBER_PROFILE_IMG, memberId));

		return ResponseEntity.ok().build();
	}

	@Override
	@PutMapping(value = "/{memberId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> updateProfileImage(@PathVariable String memberId,
		@RequestParam("file") MultipartFile file) {

		fileService.updateImage(file, FileRequest.of(FileReferenceType.MEMBER_PROFILE_IMG, memberId));

		return ResponseEntity.ok().build();
	}

	@Override
	@DeleteMapping("/{memberId}/profile-image")
	public ResponseEntity<Void> deleteProfileImage(@PathVariable String memberId) {

		fileService.deleteFile(FileRequest.of(FileReferenceType.MEMBER_PROFILE_IMG, memberId));

		return ResponseEntity.ok().build();
	}

	@Override
	@GetMapping("/{memberId}/profile-image")
	public ResponseEntity<String> getProfileImage(@PathVariable String memberId) {

		return ResponseEntity.ok(fileService.getProfileImgUrl(memberId));
	}
}
