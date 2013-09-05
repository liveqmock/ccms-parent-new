package com.yunat.ccms.channel.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.yunat.ccms.channel.support.domain.EmailBlackList;

public interface EmailBlackListRepository extends JpaRepository<EmailBlackList, Long>,
		JpaSpecificationExecutor<EmailBlackList> {

	@Query("select t from EmailBlackList t where t.contact = ?1")
	public EmailBlackList findOne(String email);

	@Query("select t from EmailBlackList t where t.contact like %?1%")
	public List<EmailBlackList> findByContactValue(String value);

	@Query("select t from EmailBlackList t where t.contact in (?1)")
	public List<EmailBlackList> findByValues(List<String> values);
}
