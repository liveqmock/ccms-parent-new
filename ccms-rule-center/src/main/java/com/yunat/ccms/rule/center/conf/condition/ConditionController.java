package com.yunat.ccms.rule.center.conf.condition;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

@Controller
@RequestMapping(value = { "/rulecenter/condition/*" }, method = RequestMethod.GET)
public class ConditionController {

	@Autowired
	protected ConditionService conditionService;

	/**
	 * 接口2.6：获取全部指标类型
	 * 
	 * @return
	 */
	@RequestMapping(value = "/type/list")
	@ResponseBody
	public Object typeList() {
		return ControlerResult.newSuccess(conditionService.conditionTypeList());
	}

	/**
	 * 接口2.7：获取特定指标类型下的指标
	 * 
	 * @param conditionTypeId
	 * @return
	 */
	@RequestMapping(value = "/type/{typeId}/property/list")
	@ResponseBody
	public Object typePropertyList(@PathVariable("typeId") final String conditionTypeId) {
		try {
			final List<?> properties = conditionService.propertysOfConditionType(conditionTypeId);
			return ControlerResult.newSuccess(properties);
		} catch (final RuleCenterRuntimeException e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

	/**
	 * 接口2.8：获取指标支持的操作符 和 可选值（又称“参考值”）
	 * 
	 * @param propertyId
	 * @return
	 */
	@RequestMapping(value = "/property/{propertyId}")
	@ResponseBody
	public Object property(@PathVariable("propertyId") final long propertyId) {
		final ConditionProperty c = conditionService.getConditionProperty(propertyId);
		return ControlerResult.newSuccess(c);
	}

	/**
	 * 接口2.11：根据上级区域的id获取它的直接下级区域列表。如根据省的id获取市列表。
	 * 
	 * @param regionId
	 * @return
	 */
	@RequestMapping("/region/{regionId}")
	@ResponseBody
	public Object region(@PathVariable("regionId") final long regionId) {
		final Collection<ProvidedValues> subRegion = conditionService.subRegion(regionId);
		return ControlerResult.newSuccess(subRegion);
	}
}
