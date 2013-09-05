package com.yunat.ccms.rule.center;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.rule.center.conf.drl.PlanDRLService;
import com.yunat.ccms.rule.center.conf.plan.Plan;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroup;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroupService;
import com.yunat.ccms.rule.center.engine.EngineState;
import com.yunat.ccms.rule.center.engine.RuleEngineSessionRepository;

@Component
public class RuleEngineBootStrap implements InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(RuleEngineBootStrap.class);

	@Autowired
	private PlanGroupService planGroupService;

	@Autowired
	private PlanDRLService planDRLService;

	@Autowired
	private RuleEngineSessionRepository ruleEngineSessionRepository;

	/**
	 * 启动时加载规则引擎
	 * 
	 * @throws RuleCenterBusinessException
	 */
	private void bootStrap() throws RuleCenterBusinessException {
		List<PlanGroup> planGroupList = planGroupService.findAll();
		if (CollectionUtils.isEmpty(planGroupList)) {
			logger.info("规则引擎没有可用店铺！");
			return;
		}
		for (PlanGroup planGroup : planGroupList) {
			String shopId = planGroup.getShopId();
			List<Plan> plans = planGroup.getPlans();
			logger.info("规则引擎:Shop:{}已配置的方案：Plans{}", shopId, plans);

			List<Plan> activePlans = new ArrayList<Plan>();
			for (Plan plan : plans) {
				if (plan.isActive()) {
					activePlans.add(plan);
					planDRLService.build(plan);
				}
			}
			if (activePlans.size() <= 0) {
				logger.info("规则引擎:店铺{}没有开启方案！", shopId);
				continue;
			}
			try {
				ruleEngineSessionRepository.refreshSession(shopId);
			} catch (RuleCenterBusinessException e) {
				// 这里不要影响别的店铺的引擎执行
				EngineState.halt(shopId);
				e.printStackTrace();
			}
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		bootStrap();
	}
}
