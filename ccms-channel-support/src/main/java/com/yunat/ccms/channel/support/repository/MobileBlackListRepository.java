package com.yunat.ccms.channel.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.channel.support.domain.MobileBlackList;

@Repository
public interface MobileBlackListRepository extends JpaRepository<MobileBlackList, String>,
		JpaSpecificationExecutor<MobileBlackList> {

	@Override
	@Query("select t from MobileBlackList t where t.contact = ?1")
	public MobileBlackList findOne(String mobile);

	@Query("select t from MobileBlackList t where t.contact like %?1%")
	public List<MobileBlackList> findByContactValue(String value);

	@Query("select t from MobileBlackList t where t.contact in (?1)")
	public List<MobileBlackList> findByValues(List<String> values);
}
