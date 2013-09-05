package com.yunat.ccms.rule.center.engine.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.FileSystemResource;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;
import com.yunat.ccms.rule.center.conf.drl.PlanDRL;
import com.yunat.ccms.rule.center.conf.drl.PlanDRLService;
import com.yunat.ccms.rule.center.conf.plan.Plan;
import com.yunat.ccms.rule.center.drl.DRLFileLocator;
import com.yunat.ccms.rule.center.engine.RuleEngineSession;
import com.yunat.ccms.rule.center.engine.RuleEngineSessionFactory;

/**
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class DefaultRuleEngineSessionFactory implements RuleEngineSessionFactory {

	private static Logger logger = LoggerFactory.getLogger(DefaultRuleEngineSessionFactory.class);

	@Autowired
	private PlanDRLService planDRLService;

	@Autowired
	private DRLFileLocator drlFileLocator;

	@Override
	public RuleEngineSession buildSession(List<Plan> plans) throws RuleCenterBusinessException {
		Assert.notEmpty(plans);
		List<PlanDRL> planDRLList = new ArrayList<PlanDRL>();
		for (Plan plan : plans) {
			PlanDRL planDrl = planDRLService.findByShopIdAndPlanId(plan.getShopId(), plan.getId());
			if (planDrl != null) {
				planDRLList.add(planDrl);
			} else {
				logger.warn("规则引擎：方案{}没有生成规则文件！", plan.getId());
			}
		}
		if (CollectionUtils.isEmpty(planDRLList)) {
			throw new RuleCenterBusinessException("规则引擎：提供的方案没有生成相应的规则文件");
		}
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		for (PlanDRL planDRL : planDRLList) {
			String shopId = planDRL.getShopId();
			Long planId = planDRL.getPlanId();
			String drlFileFullPath = drlFileLocator.getFileFullPath(shopId, planId, planDRL.getDrl());
			FileSystemResource resource = new FileSystemResource(new File(drlFileFullPath));
			kbuilder.add(resource, ResourceType.DRL);
			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error : errors) {
					logger.error(error.toString());
				}
				throw new RuleCenterBusinessException("规则引擎：解析规则异常");
			}
			logger.info("规则引擎：店铺{},方案{}，解析规则成功：{}", new Object[] { shopId, planId, drlFileFullPath });
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		logger.info("规则引擎:创建KnowledgeBase");
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		logger.info("规则引擎:添加KnowledgePackage");
		StatelessKnowledgeSession statelessSession = kbase.newStatelessKnowledgeSession();
		logger.info("规则引擎:创建StatelessKnowledgeSession");
		RuleEngineSession session = new DefaultRuleEngineSession(statelessSession);
		return session;
	}
}
