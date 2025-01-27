package com.palbang.unsemawang.common.util.file.entity;

import java.time.LocalDate;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.common.util.file.constant.FileReferenceType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class File extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String s3Key;
	private String type;
	private long size;

	private FileReferenceType referenceType;
	private Long referenceId;
	private String referenceStringId;

	private boolean isDeleted;
	private LocalDate expireDate;

	public void softDelete() {
		this.isDeleted = true;
		this.expireDate = LocalDate.now().plusDays(30);
	}
}
