package com.yunat.ccms.rule.center.status.plan;

import java.util.Date;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

public interface PlanStatusService {

	/**
	 * 某方案在某段时间内处理订单情况的统计数据.
	 * 
	 * @param planId
	 * @param from
	 * @param to
	 * @return
	 */
	PlanStatus planStatus(long planId, Date from, Date to) throws RuleCenterRuntimeException;

	/**
	 * 从某天开始(到现在),某方案处理订单情况的统计数据
	 * 
	 * @param planId
	 * @param date
	 * @return
	 */
	PlanStatus planStatusFrom(long planId, Date date) throws RuleCenterRuntimeException;

	/**
	 * 某方案在某天处理订单情况的统计数据
	 * 
	 * @param planId
	 * @param date
	 * @return
	 */
	PlanStatus planStatusOfDate(long planId, Date date) throws RuleCenterRuntimeException;

	/**
	 * 某方案从开始运行到指定的某天,处理的订单情况的统计数据.
	 * 
	 * @param planId
	 * @param date
	 * @return
	 */
	PlanStatus planStatusToDate(long planId, Date date) throws RuleCenterRuntimeException;
}
