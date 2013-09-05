package com.yunat.ccms.rule.center.conf.plan;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {

	Plan findByShopIdAndPosition(String planGroupId, int position);

	List<Plan> findByShopId(String planGroupId);
}
