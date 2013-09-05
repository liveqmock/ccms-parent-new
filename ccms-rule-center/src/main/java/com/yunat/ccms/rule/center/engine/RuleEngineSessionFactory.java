package com.yunat.ccms.rule.center.engine;

import java.util.List;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;
import com.yunat.ccms.rule.center.conf.plan.Plan;

/**
 * 创建与某个方案组的交互的session
 * 
 * @author xiaojing.qu
 * 
 */
public interface RuleEngineSessionFactory {

	/**
	 * 创建方案对应的Session
	 * 
	 * @param plans
	 *            传入的方案列表
	 * @return
	 * @throws RuleCenterBusinessException
	 */
	RuleEngineSession buildSession(List<Plan> plans) throws RuleCenterBusinessException;

}
