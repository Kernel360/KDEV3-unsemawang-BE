package com.palbang.unsemawang.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.community.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
