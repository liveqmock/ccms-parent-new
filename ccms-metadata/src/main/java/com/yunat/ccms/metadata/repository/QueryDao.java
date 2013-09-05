package com.yunat.ccms.metadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.metadata.pojo.Query;

@Repository
public interface QueryDao extends JpaRepository<Query, Long> {

	@org.springframework.data.jpa.repository.Query("select t from com.yunat.ccms.metadata.pojo.Query t where t.code = ?1")
	public Query findQueryByCode(String code);
}
