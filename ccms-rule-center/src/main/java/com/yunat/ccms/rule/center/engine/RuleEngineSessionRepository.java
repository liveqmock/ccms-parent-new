package com.yunat.ccms.rule.center.engine;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;

/**
 * 获取与某个方案组的交互的session
 * 
 * @author xiaojing.qu
 * 
 */
public interface RuleEngineSessionRepository {

	/**
	 * 获取店铺对应的Session
	 * 
	 * @param shopId
	 * @return Session（单例的）
	 */
	RuleEngineSession getSession(String shopId) throws RuleCenterBusinessException;

	/**
	 * 更新session(修改店铺的方案部署配置时调用)
	 * 
	 * @param shopId
	 * @return 更新后的session，如果更新后没有方案开启，返回null
	 * @throws RuleCenterBusinessException
	 */
	void refreshSession(String shopId) throws RuleCenterBusinessException;

}
