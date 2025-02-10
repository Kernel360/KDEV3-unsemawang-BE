package com.palbang.unsemawang.activity.repository;

import org.springframework.data.repository.CrudRepository;

import com.palbang.unsemawang.activity.entity.ActiveMember;

public interface ActiveMemberRepository extends CrudRepository<ActiveMember, String> {

}
