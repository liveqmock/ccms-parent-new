package com.yunat.ccms.rule.center.status.plan;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.rule.center.conf.plan.Plan;
import com.yunat.ccms.rule.center.conf.plan.PlanService;
import com.yunat.ccms.rule.center.conf.rule.Rule;
import com.yunat.ccms.rule.center.runtime.job.RcJobDetailLog;
import com.yunat.ccms.rule.center.runtime.job.RcJobDetailLogRepository;
import com.yunat.ccms.rule.center.runtime.job.RcJobLog;
import com.yunat.ccms.rule.center.runtime.job.RcJobService;

@Service
public class PlanStatusServiceImpl implements PlanStatusService {
	protected static final NumberFormat RATE_FORMAT = NumberFormat.getPercentInstance();

	@Autowired
	protected RcJobDetailLogRepository jobDetailLogRepository;
	@Autowired
	protected RcJobService rcJobService;
	@Autowired
	protected PlanService planService;

	@Override
	public PlanStatus planStatus(final long planId, final Date _from, final Date _to) throws RuleCenterRuntimeException {
		final Plan plan = planService.getPlan(planId);
		return planStatus(plan, _from, _to);
	}

	protected PlanStatus planStatus(final Plan plan, final Date _from, final Date _to)
			throws RuleCenterRuntimeException {
		if (!plan.isActive()) {
			throw new PlanNotActiveException();
		}
		if (_from == null || _to == null) {
			throw new RuleCenterRuntimeException("请指定时间");
		}
		// "智能"判断时间!有的api看到from比to大就抛异常,咱们比它们牛多了哈哈
		Date from = _from, to = _to;
		if (from.equals(to)) {
			return emptyResult(from, to);
		}
		if (_from.after(_to)) {
			from = _to;
			to = _from;
		}
		final Date startTime = plan.getStartTime();
		assert startTime != null;
		if (startTime.after(from)) {
			if (startTime.after(to)) {
				return emptyResult(from, to);
			}
			from = startTime;
		}

		final List<Rule> rules = plan.getRules();
		final Map<Long, RuleStatus> ruleMap = Maps.newHashMapWithExpectedSize(rules.size());
		final List<RuleStatus> ruleDatas = Lists.newArrayListWithExpectedSize(rules.size());
		for (final Rule rule : rules) {
			final RuleStatus e = new RuleStatus(rule);
			ruleDatas.add(e);
			ruleMap.put(rule.getId(), e);
		}
		// 注意:jobLog是属于店铺的,与方案无关.
		final Collection<RcJobLog> jobLogs = rcJobService.getJobLogsByShopIdEndTimeBetween(plan.getShopId(), from, to);

		int planMatched = 0;
		for (final RcJobLog jobLog : jobLogs) {
			if (jobLog.getHits() > 0 && jobLog.getCountFlag()) {
				// 规则匹配数
				final List<RcJobDetailLog> detailLogs = jobDetailLogRepository.findBytidAndPlanId(jobLog.getTid(),
						plan.getId());
				planMatched += detailLogs.size();
				for (final RcJobDetailLog log : detailLogs) {
					final RuleStatus ruleStatus = ruleMap.get(log.getRuleId());
					if (ruleStatus != null) {// 如果rule被删除了就会是null
						ruleStatus.setMatched(ruleStatus.getMatched() + 1);
					}
				}
			}
		}
		// 规则匹配占比
		for (final RuleStatus ruleStatus : ruleDatas) {
			if (planMatched == 0) {
				ruleStatus.setRate("0%");
			} else {
				final double ruleMatched = ruleStatus.getMatched();
				ruleStatus.setRate(RATE_FORMAT.format(ruleMatched / planMatched));
			}
		}

		final PlanStatus planStatus = new PlanStatus();
		planStatus.setHandled(jobLogs.size());// 方案处理数,=店铺订单数.
		planStatus.setMatched(planMatched);// 方案匹配数
		planStatus.setRuleData(ruleDatas);
		planStatus.setFrom(from);
		planStatus.setTo(to);
		return planStatus;
	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	protected PlanStatus emptyResult(final Date from, final Date to) {
		final PlanStatus planStatus = new PlanStatus();
		planStatus.setFrom(from);
		planStatus.setHandled(0);
		planStatus.setMatched(0);
		planStatus.setRuleData(Collections.<RuleStatus> emptyList());
		planStatus.setTo(to);
		return planStatus;
	}

	@Override
	public PlanStatus planStatusFrom(final long planId, final Date date) throws RuleCenterRuntimeException {
		return planStatus(planId, date, new Date());
	}

	@Override
	public PlanStatus planStatusOfDate(final long planId, final Date date) throws RuleCenterRuntimeException {
		return planStatus(planId, DateUtils.dateStart(date), DateUtils.dateEnd(date));
	}

	@Override
	public PlanStatus planStatusToDate(final long planId, final Date date) throws RuleCenterRuntimeException {
		final Plan plan = planService.getPlan(planId);
		if (!plan.isActive()) {
			throw new PlanNotActiveException();
		}
		return planStatus(plan, plan.getStartTime(), date);
	}

	public static void main(final String[] args) {
		System.out.println("@@@@@@PlanStatusServiceImpl.main():" + RATE_FORMAT.format(1 / 2));
		System.out.println("@@@@@@PlanStatusServiceImpl.main():" + RATE_FORMAT.format(1.0 / 2));

		System.out.println("@@@@@@PlanStatusServiceImpl.main():" + RATE_FORMAT.format(2.718281828));// e
		System.out.println("@@@@@@PlanStatusServiceImpl.main():" + RATE_FORMAT.format(3.1415926));// π
		System.out.println("@@@@@@PlanStatusServiceImpl.main():" + RATE_FORMAT.format(4.13566743));// 普朗克常量(电子伏秒)
		System.out.println("@@@@@@PlanStatusServiceImpl.main():" + RATE_FORMAT.format(6.62606896));// 普朗克常量(焦秒)
	}
}
