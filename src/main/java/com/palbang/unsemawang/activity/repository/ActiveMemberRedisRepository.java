package com.palbang.unsemawang.activity.repository;

import org.springframework.data.repository.CrudRepository;

import com.palbang.unsemawang.activity.entity.ActiveMember;

public interface ActiveMemberRedisRepository extends CrudRepository<ActiveMember, String> {

}
