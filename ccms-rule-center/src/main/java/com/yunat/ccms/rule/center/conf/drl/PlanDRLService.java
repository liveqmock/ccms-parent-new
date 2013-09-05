package com.yunat.ccms.rule.center.conf.drl;

import java.util.List;

import com.yunat.ccms.rule.center.conf.plan.Plan;

/**
 * 方案对应规则的服务层
 * 
 * @author tao.yang
 * 
 */
public interface PlanDRLService {

	/**
	 * 按照方案的配置生成规则文件，并且记录changelog
	 * 
	 * @param plan
	 * @return
	 */
	PlanDRL build(final Plan plan);

	/**
	 * 移除方案的规则文件，并且记录changelog
	 * 
	 * @param plan
	 */
	void remove(final Plan plan);

	/**
	 * 查询符合条件的方案规则
	 * 
	 * @param shopId
	 * @param planId
	 * @return
	 */
	PlanDRL findByShopIdAndPlanId(final String shopId, final Long planId);

	/**
	 * 查询店铺的方案列表
	 * 
	 * @param shopId
	 * @param planId
	 * @return
	 */
	List<PlanDRL> findByShopId(final String shopId);
}
