package com.yunat.ccms.rule.center.conf.condition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.core.support.vo.IdName;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.rule.center.conf.rule.Rule;

public interface ConditionService {

	/**
	 * 将属于rule的condition填充到rule里
	 * 
	 * @param rule
	 * @throws RuleCenterRuntimeException
	 */
	public void fillCondition(Rule... rule) throws RuleCenterRuntimeException;

	/**
	 * 将属于rule的condition填充到rule里
	 * 
	 * @param rules
	 * @throws RuleCenterRuntimeException
	 */
	public void fillCondition(Collection<Rule> rules) throws RuleCenterRuntimeException;

	/**
	 * 获取特定指标类型下的指标
	 * 
	 * @param conditionTypeId
	 * @return
	 * @throws RuleCenterRuntimeException
	 */
	public List<IdName> propertysOfConditionType(String conditionTypeId) throws RuleCenterRuntimeException;

	/**
	 * 获取指标支持的操作符 和 可选值
	 * 
	 * @param propertyId
	 * @return
	 * @throws RuleCenterRuntimeException
	 */
	public ConditionProperty getConditionProperty(final long propertyId) throws RuleCenterRuntimeException;

	/**
	 * 获取全部指标类型
	 * 
	 * @return
	 */
	public Collection<Map<String, Object>> conditionTypeList();

	/**
	 * 根据上级区域的id获取它的直接下级区域列表。如根据省的id获取市列表。
	 * 
	 * @param regionId
	 * @return
	 */
	public Collection<ProvidedValues> subRegion(long regionId);
}
