package com.yunat.ccms.node.biz.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NodeQueryCriteriaRepository extends JpaRepository<NodeQueryCriteria, Long> {

	@Modifying
	@Transactional
	@Query("delete from NodeQueryCriteria u where u.nodeQueryDefined = ?1")
	public void deleteCriteriaByQuery(NodeQueryDefined defined);

	@Modifying
	@Transactional
	@Query("delete from NodeQueryCriteria u where u.nodeQueryDefined.id = ?1")
	public void deleteCriteriaByQuery(Long id);
}
