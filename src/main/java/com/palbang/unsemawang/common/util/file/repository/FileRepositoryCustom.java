package com.palbang.unsemawang.common.util.file.repository;

import java.util.List;

import com.palbang.unsemawang.common.util.file.dto.FileRequest;
import com.palbang.unsemawang.common.util.file.entity.File;

public interface FileRepositoryCustom {
	List<File> findFilesByFileReference(FileRequest fileRequest);

	List<File> findFilesByFileReferenceAndIsNotDeleted(FileRequest fileRequest);
}
