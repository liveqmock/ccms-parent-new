package com.yunat.ccms.rule.center.conf.condition;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ConditionRepository extends CrudRepository<Condition, Long> {

	List<Condition> findByRuleIdIn(Collection<Long> ruleIds);

	@Transactional
	@Query("delete from Condition c where c.ruleId in(?1)")
	@Modifying
	void deleteByRuleIdIn(Collection<Long> ruleIds);
}
