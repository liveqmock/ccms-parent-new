package com.yunat.ccms.metadata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.metadata.pojo.QueryCriteria;

@Repository
public interface QueryCriteriaDao extends JpaRepository<QueryCriteria, Long> {

	@Query("select t from QueryCriteria t where queryCriteriaId in (?1)")
	public List<QueryCriteria> findByKeys(List<Long> keys);
}
