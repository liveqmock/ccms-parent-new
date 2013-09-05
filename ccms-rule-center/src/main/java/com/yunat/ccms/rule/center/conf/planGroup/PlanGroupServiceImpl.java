package com.yunat.ccms.rule.center.conf.planGroup;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.yunat.ccms.rule.center.RuleCenterCons;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.rule.center.conf.ContentTooLongException;
import com.yunat.ccms.rule.center.conf.plan.Plan;
import com.yunat.ccms.rule.center.conf.plan.PlanRepository;
import com.yunat.ccms.rule.center.conf.plan.PlanService;
import com.yunat.ccms.rule.center.conf.rule.Rule;
import com.yunat.ccms.rule.center.memo.MemoContentBuilder;

/**
 * 提供对方案组的一些操作服务
 * 
 * @author wenjian.liang
 */
@Service
public class PlanGroupServiceImpl implements PlanGroupService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected PlanGroupRepository planGroupRepository;

	@Autowired
	protected PlanService planService;

	@Autowired
	protected PlanRepository planRepository;

	@Autowired
	protected MemoContentBuilder remarkContentBuilder;

	@Override
	public boolean saveSign(final String planGroupId, final String sign) throws RuleCenterRuntimeException {
		if (!StringUtils.hasText(sign)) {
			throw new SignEmptyException();
		}
		if (sign.length() > RuleCenterCons.SIGN_MAX_LEN) {
			throw new ContentTooLongException("默认签名", sign, RuleCenterCons.SIGN_MAX_LEN);
		}
		final PlanGroup planGroup = planGroupOfShop(planGroupId);
		final String oldSign = planGroup.getSign();
		if (sign.equals(oldSign)) {// 值是一样的
			logger.debug("新的签名与原有签名是一致的.原有签名:" + oldSign + ",新签名:" + sign);
			return true;
		}
		planGroup.setSign(sign);
		try {
			planGroupRepository.save(planGroup);
			return true;
		} catch (final Exception e) {
			logger.warn("保存异常", e);
			return false;
		}
	}

	@Override
	public String previewSign(final String planGroupId) throws RuleCenterRuntimeException {
		final PlanGroup planGroup = planGroupOfShop(planGroupId);
		final Collection<Plan> plans = planGroup.getPlans();
		final Collection<Rule> matchedRules = Lists.newArrayListWithExpectedSize(plans.size());
		for (final Plan plan : plans) {
			if (plan.isActive()) {
				final List<Rule> rules = plan.getRules();
				if (rules != null && !rules.isEmpty()) {
					matchedRules.add(rules.get(0));
				}
			}
		}
		return remarkContentBuilder.buildMemoContent(planGroup, matchedRules);
	}

	@Override
	public PlanGroup planGroupOfShop(final String shopId) throws RuleCenterRuntimeException, IllegalArgumentException {
		Assert.hasText(shopId, "店铺id不能为空");

		PlanGroup planGroup = planGroupRepository.findOne(shopId);
		if (planGroup == null) {
			planGroup = new PlanGroup();
			planGroup.setShopId(shopId);
			planGroup.setSign(RuleCenterCons.DEFAULT_SIGN);
			planGroup = planGroupRepository.save(planGroup);
			// 生成默认的方案列表
			final List<Plan> plans = Lists.newArrayListWithExpectedSize(RuleCenterCons.MAX_PLANS_COUNT_OF_SHOP);
			for (int i = 1; i <= RuleCenterCons.MAX_PLANS_COUNT_OF_SHOP; ++i) {
				final Plan plan = new Plan();
				plan.setActive(false);
				plan.setId(null);
				plan.setName("方案" + i);
				plan.setPosition(i);
				plan.setShopId(planGroup.getShopId());
				plans.add(plan);
			}
			planRepository.save(plans);
			planGroup.setPlans(plans);
		} else {
			planService.fillPlan(planGroup);
		}
		return planGroup;
	}

	@Override
	public List<PlanGroup> findAll() {
		final List<PlanGroup> planGroupList = planGroupRepository.findAll();
		if (CollectionUtils.isEmpty(planGroupList)) {
			return null;
		}
		for (final PlanGroup planGroup : planGroupList) {
			planService.fillPlan(planGroup);
		}
		return planGroupList;
	}
}
