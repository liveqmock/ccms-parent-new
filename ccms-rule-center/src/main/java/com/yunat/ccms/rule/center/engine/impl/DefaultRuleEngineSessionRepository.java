package com.yunat.ccms.rule.center.engine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;
import com.yunat.ccms.rule.center.conf.plan.Plan;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroup;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroupService;
import com.yunat.ccms.rule.center.engine.EngineState;
import com.yunat.ccms.rule.center.engine.RuleEngineSession;
import com.yunat.ccms.rule.center.engine.RuleEngineSessionFactory;
import com.yunat.ccms.rule.center.engine.RuleEngineSessionRepository;

@Component
public class DefaultRuleEngineSessionRepository implements RuleEngineSessionRepository {

	private static Logger logger = LoggerFactory.getLogger(DefaultRuleEngineSessionRepository.class);

	private ConcurrentHashMap<String, RuleEngineSession> sessionCache = new ConcurrentHashMap<String, RuleEngineSession>();

	@Autowired
	private RuleEngineSessionFactory sessionFactory;

	@Autowired
	private PlanGroupService planGroupService;

	@Override
	public RuleEngineSession getSession(String shopId) throws RuleCenterBusinessException {
		if (StringUtils.isEmpty(shopId)) {
			throw new RuleCenterBusinessException("店铺ID不能为空");
		}
		RuleEngineSession session = sessionCache.get(shopId);
		if (EngineState.avaliable(shopId) && session != null) {
			return session;
		}
		throw new RuleCenterBusinessException("规则引擎：店铺" + shopId + "未初始化");
	}

	@Override
	public void refreshSession(String shopId) throws RuleCenterBusinessException {
		PlanGroup planGroup = planGroupService.planGroupOfShop(shopId);
		if (planGroup == null || CollectionUtils.isEmpty(planGroup.getPlans())) {
			logger.error("店铺:{}没有配置好方案!", shopId);
			stopEngine(shopId);
			return;
		}
		List<Plan> activePlan = new ArrayList<Plan>();
		for (Plan plan : planGroup.getPlans()) {
			if (plan.isActive()) {
				activePlan.add(plan);
			}
		}
		if (CollectionUtils.isEmpty(activePlan)) {
			logger.error("店铺:{}没有配置好方案", shopId);
			stopEngine(shopId);
			return;
		}
		try {
			RuleEngineSession session = sessionFactory.buildSession(activePlan);
			sessionCache.put(shopId, session);
			EngineState.start(shopId);
			return;
		} catch (RuleCenterBusinessException e) {
			e.printStackTrace();
			throw new RuleCenterBusinessException(e.getMessage());
		}
	}

	private void stopEngine(String shopId) {
		sessionCache.remove(shopId);
		EngineState.halt(shopId);
	}
}
