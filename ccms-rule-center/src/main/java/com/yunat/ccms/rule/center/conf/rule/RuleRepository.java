package com.yunat.ccms.rule.center.conf.rule;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RuleRepository extends CrudRepository<Rule, Long> {

	List<Rule> findByPlanId(long planId);

	List<Rule> findByPlanIdIn(Collection<Long> planIds);

	@Transactional
	@Query("delete from Rule r where r.planId=?1")
	@Modifying
	void deleteByPlanId(long planId);

	@Query(
			value = "select r.* from rc_rule r,rc_plan p where r.plan_id=p.id and r.id in ?1 order by p.position,r.position ASC ",
			nativeQuery = true)
	List<Rule> findOrderedRule(Set<Long> ruleIds);
}
