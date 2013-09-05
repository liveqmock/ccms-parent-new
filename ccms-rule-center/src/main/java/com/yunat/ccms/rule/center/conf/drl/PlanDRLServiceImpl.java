package com.yunat.ccms.rule.center.conf.drl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.login.NeedLoginException;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.rule.center.conf.plan.Plan;
import com.yunat.ccms.rule.center.conf.rule.Rule;
import com.yunat.ccms.rule.center.conf.rule.RuleService;
import com.yunat.ccms.rule.center.drl.DRLFileBuilder;
import com.yunat.ccms.rule.center.drl.DRLFileLocator;

@Component
public class PlanDRLServiceImpl implements PlanDRLService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlanDRLRepository planDRLrepository;

	@Autowired
	private PlanDRLChangelogRepository planDRLChangelogRepository;

	@Autowired
	private DRLFileLocator drlFileLocator;

	@Autowired
	private DRLFileBuilder drlFileBuilder;

	@Autowired
	private RuleService ruleService;

	public PlanDRL build(final Plan plan) {
		String shopId = plan.getShopId();
		Long planId = plan.getId();
		logger.info("规则引擎:Shop:{}重新生成Plan:{}的规则文件", shopId, planId);
		// 给方案填充上rule/condition 其实plan里已经有rules了
		final Collection<Rule> rules = plan.getRules();
		if (rules == null || rules.isEmpty()) {
			ruleService.fillRules(plan);
		}
		// 生成drl文件,然后写入PlanDRL
		final String fileName = drlFileBuilder.createDRLFile(plan);
		final PlanDRL planDrl = new PlanDRL();
		planDrl.setPlanId(plan.getId());
		planDrl.setShopId(plan.getShopId());
		planDrl.setDrl(fileName);
		planDrl.setStartTime(new Date());

		Long operatorId = getOperatorId();
		logger.info("当前用户ID：{}", operatorId);
		PlanDRL originalPlanDRL = planDRLrepository.findByShopIdAndPlanId(shopId, planId);
		if (originalPlanDRL != null) {
			originalPlanDRL.setOperatorId(operatorId);
			planDRLChangelogRepository.save(new PlanDRLChangelog(originalPlanDRL));
			planDRLrepository.delete(originalPlanDRL);
			drlFileLocator.backUp(originalPlanDRL.getShopId(), originalPlanDRL.getDrl());
		}
		planDrl.setOperatorId(operatorId);
		return planDRLrepository.save(planDrl);
	}

	@Override
	public void remove(final Plan plan) {
		if (plan == null) {
			return;
		}
		String shopId = plan.getShopId();
		Long planId = plan.getId();
		PlanDRL planDRL = planDRLrepository.findByShopIdAndPlanId(shopId, planId);
		if (planDRL == null) {
			return;
		}
		Long operatorId = getOperatorId();
		if (operatorId != null) {
			planDRL.setOperatorId(operatorId);
		}
		planDRLChangelogRepository.save(new PlanDRLChangelog(planDRL));
		planDRLrepository.delete(planDRL);
		drlFileLocator.backUp(planDRL.getShopId(), planDRL.getDrl());
	}

	private Long getOperatorId() {
		try {
			User user = LoginInfoHolder.getCurrentUser();
			return user.getId();
		} catch (NeedLoginException e) {
			return null;
		}
	}

	@Override
	public PlanDRL findByShopIdAndPlanId(final String shopId, final Long planId) {
		return planDRLrepository.findByShopIdAndPlanId(shopId, planId);
	}

	@Override
	public List<PlanDRL> findByShopId(String shopId) {
		return planDRLrepository.findByShopId(shopId);
	}
}