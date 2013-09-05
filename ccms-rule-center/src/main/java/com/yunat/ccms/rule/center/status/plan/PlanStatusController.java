package com.yunat.ccms.rule.center.status.plan;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

@Controller
@RequestMapping(method = RequestMethod.GET,//
		value = { "/rulecenter/status/plan/{planId:[0-9]+}",//
				"/rulecenter/statuses/plans/{planId:[0-9]+}" })
public class PlanStatusController {

	protected static final String DATE_SLOT_PATTERN = "yyyyMMdd";
	@Autowired
	protected PlanStatusService planStatusService;

	/**
	 * 接口3.1：当前方案监控(今日)
	 * 
	 * @param planId
	 * @param dateSlot examples:
	 *            /rulecenter/statuses/plans/1/slot/20130613 ：获取方案1在2013年6月13日当天的执行数据
	 *            /rulecenter/statuses/plans/1/slot/20130613-20130615 ：获取方案1从2013年6月13日零点整开始到2013年6月15日23点59分59秒的执行数据
	 * @return
	 */
	@RequestMapping("/slot/{dateSlot}")
	@ResponseBody
	public Object slot(@PathVariable("planId") final long planId, @PathVariable("dateSlot") final String dateSlot) {
		try {
			PlanStatus planStatus;
			if (StringUtils.hasText(dateSlot)) {
				final Date[] fromTo = parseFromTo(dateSlot);
				if (fromTo.length == 1) {
					planStatus = planStatusService.planStatusOfDate(planId, fromTo[0]);
				} else {
					planStatus = planStatusService.planStatus(planId, fromTo[0], fromTo[1]);
				}
			} else {//其实不会到达
				planStatus = planStatusService.planStatusToDate(planId, new Date());
			}
			return ControlerResult.newSuccess(planStatus);
		} catch (final RuleCenterRuntimeException e) {
			return ControlerResult.newError(e.getMessage());
		}
	}

	/**
	 * 接口3.2：当前方案监控(累计)
	 * 若该方案未开启,返回表示"空概念"的结果;
	 * 若该方案已开启,返回从开启时间到现在的累计处理结果信息.
	 * 
	 * @param planId
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object accumulation(@PathVariable("planId") final long planId) {
		final PlanStatus planStatus = planStatusService.planStatusToDate(planId, new Date());
		return ControlerResult.newSuccess(planStatus);
	}

	protected Date[] parseFromTo(final String dateSlot) {
		final int minusIndex = dateSlot.indexOf('-');
		if (minusIndex < 0) {
			return new Date[] { DateUtils.parse(dateSlot, DATE_SLOT_PATTERN) };
		} else {
			final String fromStr = dateSlot.substring(0, minusIndex).trim();
			final String toStr = dateSlot.substring(minusIndex + 1).trim();
			return new Date[] { DateUtils.parse(fromStr, DATE_SLOT_PATTERN), DateUtils.parse(toStr, DATE_SLOT_PATTERN) };
		}
	}
}
