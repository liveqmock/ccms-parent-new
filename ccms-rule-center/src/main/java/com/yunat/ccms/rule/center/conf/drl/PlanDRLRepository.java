package com.yunat.ccms.rule.center.conf.drl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.rule.center.conf.drl.PlanDRL.PlanDRLPK;

/**
 * 方案对应的规则记录的仓储
 * 
 * @author tao.yang
 * 
 */
public interface PlanDRLRepository extends JpaRepository<PlanDRL, PlanDRLPK> {

	List<PlanDRL> findByShopId(final String shopId);

	PlanDRL findByShopIdAndPlanId(final String shopId, final Long planId);

}
