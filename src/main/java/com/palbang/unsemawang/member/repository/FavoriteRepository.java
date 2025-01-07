package com.palbang.unsemawang.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.member.entity.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
