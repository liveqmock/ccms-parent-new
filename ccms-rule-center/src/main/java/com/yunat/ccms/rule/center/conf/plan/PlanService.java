package com.yunat.ccms.rule.center.conf.plan;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroup;

/**
 * @author wenjian.liang
 */
public interface PlanService {

	/**
	 * 保存方案
	 * 
	 * @param plan
	 * @return
	 * @throws RuleCenterRuntimeException
	 */
	public boolean save(Plan plan) throws RuleCenterRuntimeException, IllegalArgumentException;

	/**
	 * 设置某方案在方案组中的位置
	 * 
	 * @param planId
	 * @param index
	 * @return 是否成功
	 * @throws RuleCenterRuntimeException
	 */
	public boolean setIndex(final long planId, final int index) throws RuleCenterRuntimeException;

	/**
	 * 开启某个方案
	 * 
	 * @param planId
	 * @return 是否成功
	 * @throws RuleCenterRuntimeException
	 */
	public Plan turnOn(final long planId) throws RuleCenterRuntimeException;

	/**
	 * 关闭某个方案
	 * 
	 * @param planId
	 * @return 是否成功
	 * @throws RuleCenterRuntimeException
	 */
	public Plan turnOff(long planId) throws RuleCenterRuntimeException;

	/**
	 * 方案预演
	 * 
	 * @param planIds
	 * @return TODO
	 * @throws RuleCenterRuntimeException
	 */
	public Object previewPlans(long... planIds) throws RuleCenterRuntimeException;

	/**
	 * 获取某个方案
	 * 
	 * @param planId
	 * @return
	 * @throws RuleCenterRuntimeException
	 */
	public Plan getPlan(long planId) throws RuleCenterRuntimeException;

	/**
	 * 将某方案的开启/关闭状态置为相反
	 * 
	 * @param planId
	 * @return
	 * @throws RuleCenterRuntimeException
	 */
	public Plan toggleOnOff(long planId) throws RuleCenterRuntimeException;

	public void fillPlan(PlanGroup planGroup) throws RuleCenterRuntimeException;
}
