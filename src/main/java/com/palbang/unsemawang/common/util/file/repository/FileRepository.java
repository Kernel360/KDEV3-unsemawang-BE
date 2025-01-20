package com.palbang.unsemawang.common.util.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.common.util.file.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long>, FileRepositoryCustom {
}
