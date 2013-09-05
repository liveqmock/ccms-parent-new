package com.yunat.ccms.rule.center.conf.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

@Controller
@RequestMapping({ "/rulecenter/plan/*", "/rulecenter/plan" })
public class PlanController {

	@Autowired
	protected PlanService planService;

	/**
	 * 保存方案(增加/修改)
	 * 
	 * @param plan
	 * @return
	 */
	@RequestMapping(value = "/{id:[0-9]+}", method = { RequestMethod.POST },
			produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object save(@RequestBody final Plan plan) {
		try {
			final boolean success = planService.save(plan);
			return success ? ControlerResult.newSuccess() : ControlerResult.newError();
		} catch (final RuleCenterRuntimeException e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

	/**
	 * 方案预演
	 * 
	 * @param planId
	 * @return
	 */
	@RequestMapping(value = "/{id:[0-9]+}/preview", method = { RequestMethod.GET })
	@ResponseBody
	public Object preview(@PathVariable("id") final long planId) {
		try {
			final Object result = planService.previewPlans(planId);
			return ControlerResult.newSuccess(result);
		} catch (final RuleCenterRuntimeException e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

	/**
	 * 修改方案.由于需要restful,/{id}-PUT方法只能有一个方法,因此这个方法是一个分派方法
	 * 
	 * @param planId
	 * @param plan
	 * @return
	 */
	@RequestMapping(value = "/{id:[0-9]+}", method = { RequestMethod.PUT })
	@ResponseBody
	public Object updatePlan(@PathVariable("id") final long planId,//
			@RequestBody final Plan plan) {
		try {
			final Plan newPlan = onOff(planId, plan.isActive());
			return ControlerResult.newSuccess(newPlan);
		} catch (final Exception e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

	/**
	 * 调整方案顺序
	 * 
	 * @param planId
	 * @param position
	 * @return
	 */
	protected boolean reorderPlan(final long planId, final int position) {
		return planService.setIndex(planId, position);
	}

	/**
	 * 开启/关闭方案
	 * 
	 * @param planId
	 * @param on
	 * @return
	 */
	protected Plan onOff(final long planId, final Boolean on) {
		if (on == null) {
			return planService.toggleOnOff(planId);
		} else if (on) {
			return planService.turnOn(planId);
		} else {
			return planService.turnOff(planId);
		}
	}
}
