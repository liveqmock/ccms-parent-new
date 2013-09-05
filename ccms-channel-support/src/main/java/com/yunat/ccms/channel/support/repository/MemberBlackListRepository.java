package com.yunat.ccms.channel.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.yunat.ccms.channel.support.domain.MemberBlackList;

public interface MemberBlackListRepository extends JpaRepository<MemberBlackList, Long>,
		JpaSpecificationExecutor<MemberBlackList> {

	@Query("select t from MemberBlackList t where t.contact = ?1")
	public MemberBlackList findOne(String member);

	@Query("select t from MemberBlackList t where t.contact like %?1%")
	public List<MemberBlackList> findByContactValue(String value);

	@Query("select t from MemberBlackList t where t.contact in (?1)")
	public List<MemberBlackList> findByValues(List<String> values);
}
