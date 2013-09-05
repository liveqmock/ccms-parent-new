package com.yunat.ccms.rule.center.conf.plan;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.yunat.ccms.core.support.utils.ListUtils;
import com.yunat.ccms.rule.center.RuleCenterCons;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.rule.center.conf.ContentTooLongException;
import com.yunat.ccms.rule.center.conf.Util;
import com.yunat.ccms.rule.center.conf.condition.Condition;
import com.yunat.ccms.rule.center.conf.condition.ConditionOp;
import com.yunat.ccms.rule.center.conf.condition.ConditionRepository;
import com.yunat.ccms.rule.center.conf.condition.ConditionType;
import com.yunat.ccms.rule.center.conf.drl.PlanDRLService;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroup;
import com.yunat.ccms.rule.center.conf.rule.Rule;
import com.yunat.ccms.rule.center.conf.rule.RuleRepository;
import com.yunat.ccms.rule.center.conf.rule.RuleService;
import com.yunat.ccms.rule.center.engine.RuleEngineSessionRepository;

/**
 * 提供对方案的一些操作服务
 * 
 * @author wenjian.liang
 */
@Service
public class PlanServiceImpl implements PlanService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SavePlanHelper savePlanHelper = new SavePlanHelper();

	@Autowired
	protected RuleService ruleService;
	@Autowired
	protected PlanRepository planRepository;
	@Autowired
	protected RuleRepository ruleRepository;
	@Autowired
	protected ConditionRepository conditionRepository;
	@Autowired
	private PlanDRLService planDrlService;
	@Autowired
	private RuleEngineSessionRepository ruleEngineSessionRepository;

	@Override
	public boolean setIndex(final long planId, final int position) throws RuleCenterRuntimeException {
		final Plan plan = getPlan(planId);
		if (plan.getPosition() == position) {// 已经是这个位置.
			return true;
		}
		final int oldPosition = plan.getPosition();
		plan.setPosition(position);
		// 先前在index位置的plan也得改位置
		final Plan otherPlan = planRepository.findByShopIdAndPosition(plan.getShopId(), position);
		otherPlan.setPosition(oldPosition);
		try {
			planRepository.save(Arrays.asList(plan, otherPlan));
			return true;
		} catch (final Exception e) {
			// 抛异常还是返回false?
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object previewPlans(final long... planIds) throws RuleCenterRuntimeException {
		// TODO Auto-generated method stub 这个方法可能放到engine包里更好
		return null;
	}

	@Override
	public Plan turnOn(final long planId) throws RuleCenterRuntimeException {
		final Plan plan = getPlan(planId);
		if (plan.isActive()) {// 已经是开启状态.
			return plan;
		}
		List<Rule> rules = plan.getRules();
		if (rules == null || rules.isEmpty()) {
			rules = ruleRepository.findByPlanId(planId);
			if (rules == null || rules.isEmpty()) {
				throw new PlanNoRulesException();
			}
		}
		plan.setActive(true);
		plan.setStartTime(new Date());
		return savePlanHelper.saveOnOff(plan);
	}

	@Override
	public Plan turnOff(final long planId) throws RuleCenterRuntimeException {
		final Plan plan = getPlan(planId);
		if (!plan.isActive()) {// 已经是关闭状态.
			return plan;
		}
		plan.setActive(false);
		return savePlanHelper.saveOnOff(plan);
	}

	@Override
	public Plan getPlan(final long planId) throws RuleCenterRuntimeException {
		final Plan plan = planRepository.findOne(planId);
		if (plan == null) {
			logger.warn("没有这个方案:" + planId);
			throw new NoSuchPlanException();
		}
		// 填充规则
		ruleService.fillRules(plan);
		return plan;
	}

	@Override
	public Plan toggleOnOff(final long planId) throws RuleCenterRuntimeException {
		final Plan plan = getPlan(planId);
		plan.setActive(!plan.isActive());
		return savePlanHelper.saveOnOff(plan);
	}

	@Override
	public boolean save(final Plan plan) throws RuleCenterRuntimeException, IllegalArgumentException {
		if (plan.getId() != null && plan.getId() == 0) {
			plan.setId(null);
		}
		plan.setLastConfigTime(new Date());

		final List<Plan> existPlans = planRepository.findByShopId(plan.getShopId());

		validate(plan, existPlans);

		try {
			final Plan oldPlan = ListUtils.find(existPlans, plan);
			ruleService.fillRules(oldPlan);

			if (plan.getId() == null) {// 新增.现在基本不会到这里,因为方案组初始化时会同时初始化几个方案.
				// 要不要检查店铺的方案数超标?
			} else {
				// 先删除在Plan里已经不存在的规则和条件
				final List<Rule> oldRules = oldPlan.getRules();
				final List<Rule> rules = plan.getRules();

				final Collection<Rule> rulesNeedToDel = new LinkedList<Rule>();
				final Collection<Condition> conditionsNeedToDel = new LinkedList<Condition>();
				for (final Rule oldRule : oldRules) {// 遍历原有的规则,如果不存在新的plan中,则需要删除之(连同条件)
					final Rule newRule = ListUtils.find(rules, oldRule);
					if (newRule == null) {// 此oldRule被删除
						rulesNeedToDel.add(oldRule);
						conditionsNeedToDel.addAll(oldRule.getConditions());
					} else {
						for (final Condition c : oldRule.getConditions()) {
							if (!newRule.getConditions().contains(c)) {
								conditionsNeedToDel.add(c);
							}
						}
					}
				}
				savePlanHelper.delRulesAndConditionsThoseAreRemoved(rulesNeedToDel, conditionsNeedToDel);
			}
			plan.setStartTime(null);
			plan.setActive(false);
			plan.setPosition(oldPlan.getPosition());// "保存方案"时不让改位置
			savePlanHelper.save(plan);
			return true;
		} catch (final RuleCenterRuntimeException e) {
			throw e;
		} catch (final Exception e) {// TODO:抛异常还是返回false?
			e.printStackTrace();
			return false;
		}
	}

	protected void checkString(final String content, final String itemName, final int maxLen)
			throws RuleCenterRuntimeException, IllegalArgumentException {
		Assert.hasText(content, "请填写" + itemName);
		if (content.length() > maxLen) {
			throw new ContentTooLongException(itemName, content, maxLen);
		}
	}

	protected static String reduceWhiteChars(final String s) {
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		final int len = s.length();
		boolean prevIsWhitespace = false;
		for (; i < len; ++i) {
			final char c = s.charAt(i);
			if (Character.isWhitespace(c)) {
				if (!prevIsWhitespace) {
					prevIsWhitespace = true;
					sb.append(" ");
				}
			} else {
				prevIsWhitespace = false;
				sb.append(c);
			}
		}
		return sb.toString().trim();
	}

	/**
	 * @param plan
	 * @return
	 */
	protected void validate(final Plan plan, final List<Plan> existPlans) throws RuleCenterRuntimeException,
			IllegalArgumentException {
		// 检查该方案的开启状态
		for (final Plan existPlan : existPlans) {
			if (existPlan.getId().equals(plan.getId()) && existPlan.isActive()) {
				throw new PlanIsRunningException(existPlan);
			}
		}
		// 方案必须属于某个店铺
		final String planGroupId = plan.getShopId();
		if (!StringUtils.hasText(planGroupId)) {
			throw new RuleCenterRuntimeException("必须指定方案所属的店铺。");
		}
		// 检查方案名字长度
		final String planName = plan.getName();
		checkString(planName, "方案名字", RuleCenterCons.PLAN_NAME_MAX_LEN);
		// 检查方案名字是否重复
		for (final Plan existPlan : existPlans) {
			if (!existPlan.getId().equals(plan.getId()) && existPlan.getName().equalsIgnoreCase(planName)) {
				throw new RuleCenterRuntimeException("方案名“" + planName + "”已存在。");
			}
		}
		// 检查方案有无规则
		final List<Rule> rules = plan.getRules();
		// 无规则的方案是允许的
		if (rules.size() > RuleCenterCons.MAX_RULES_COUNT_OF_PLAN) {
			throw new RuleCenterRuntimeException("一个方案最多包含" + RuleCenterCons.MAX_RULES_COUNT_OF_PLAN + "个规则");
		}
		// 检查规则
		final Collection<String> ruleNames = Lists.newArrayListWithExpectedSize(rules.size());
		int index = 0;
		for (final Rule rule : rules) {
			// 检查规则名字:1,长度;2,同方案内规则名字不可重复
			final String ruleName = rule.getName();
			checkString(ruleName, "规则名字", RuleCenterCons.RULE_NAME_MAX_LEN);
			final int ruleNameIndex = ListUtils.findIndex(ruleNames, ruleName);
			if (ruleNameIndex >= 0) {
				throw new RuleCenterRuntimeException("规则" + (ruleNameIndex + 1) + "和规则" + (index + 1) + "的名称“"
						+ ruleName + "”重复");
			}
			ruleNames.add(ruleName);

			final String remarkContent = rule.getRemarkContent();
			final String handledRemarkContent = reduceWhiteChars(remarkContent);// 将备注中的多个空白字符弄成一个
			checkString(handledRemarkContent, "规则" + rule.getName() + "的备注内容", RuleCenterCons.REMARK_CONTENT_MAX_LEN);
			rule.setRemarkContent(handledRemarkContent);

			final List<Condition> conditions = rule.getConditions();
			// 检查规则有无条件
			if (conditions == null || conditions.isEmpty()) {
				throw new RuleCenterRuntimeException("规则" + rule.getName() + "必须编写条件。");
			}
			if (conditions.size() > RuleCenterCons.MAX_CONDITIONS_COUNT_OF_RULE) {
				throw new RuleCenterRuntimeException("一个规则最多包含" + RuleCenterCons.MAX_CONDITIONS_COUNT_OF_RULE + "个条件");
			}
			// 检查条件名称
			for (final Condition condition : conditions) {
				if (!StringUtils.hasText(condition.getName())) {
					if (!condition.getUseDefaultName()) {
						throw new RuleCenterRuntimeException("请填写条件名称");
					}
					final ConditionType conditionType = ConditionType.valueOfIgnoreCase(condition.getType());
					final ConditionOp conditionOp = ConditionOp.valueOf(condition.getConditionOpName());
					condition.setName(conditionType.getTypeName() + conditionOp.getLabel()
							+ condition.getReferenceValue());
				}
			}// for condition
			++index;
		}// for rule
	}

	@Override
	public void fillPlan(final PlanGroup planGroup) throws RuleCenterRuntimeException {
		final List<Plan> plans = planRepository.findByShopId(planGroup.getShopId());
		Collections.sort(plans, Util.POSITION_COMPARATOR);
		ruleService.fillRules(plans);
		planGroup.setPlans(plans);
	}

	/**
	 * @param plan
	 */
	protected void saveRules(final Plan plan) throws RuleCenterRuntimeException {
		final Long planId = plan.getId();

		final List<Rule> rules = plan.getRules();
		int rulePosition = 1;
		for (final Rule rule : rules) {
			rule.setPosition(rulePosition);
			rule.setPlanId(planId);
			rule.setLastConfigTime(plan.getLastConfigTime());

			rulePosition++;

			final Rule savedRule = ruleRepository.save(rule);
			rule.setId(savedRule.getId());

			// 处理条件
			saveConditions(rule);
		}
	}

	/**
	 * @param rule
	 */
	protected void saveConditions(final Rule rule) throws RuleCenterRuntimeException {
		final List<Condition> conditions = rule.getConditions();
		int conditionPosition = 1;
		for (final Condition condition : conditions) {
			condition.setLastConfigTime(rule.getLastConfigTime());
			condition.setPosition(conditionPosition);
			condition.setRuleId(rule.getId());

			conditionPosition++;
		}

		conditionRepository.save(conditions);
	}

	/**
	 * 这个类是为了@Transactional. spring的aop对于某类自己调用自己的方法,是不aop的,所以要新写一个类
	 * 
	 * @author wenjian.liang
	 */
	protected class SavePlanHelper {
		@Transactional
		protected void save(final Plan plan) {
			final Plan p = planRepository.save(plan);
			if (plan.getId() == null) {
				plan.setId(p.getId());
			}
			// 处理规则
			saveRules(plan);
		}

		@Transactional
		protected Plan saveOnOff(final Plan plan) throws RuleCenterRuntimeException {
			try {
				final Plan newPlan = planRepository.save(plan);
				if (plan.isActive()) {
					planDrlService.build(plan);
				} else {
					planDrlService.remove(plan);
				}
				ruleEngineSessionRepository.refreshSession(plan.getShopId());
				return newPlan;
			} catch (final Exception e) {
				logger.error("", e);
				throw new RuleCenterRuntimeException("操作失败，请稍后重试。", e);

			}
		}

		/**
		 * @param rulesNeedToDel
		 * @param conditionsNeedToDel
		 */
		@Transactional
		protected void delRulesAndConditionsThoseAreRemoved(final Collection<Rule> rulesNeedToDel,
				final Collection<Condition> conditionsNeedToDel) {
			if (!conditionsNeedToDel.isEmpty()) {
				conditionRepository.delete(conditionsNeedToDel);
			}
			if (!rulesNeedToDel.isEmpty()) {
				ruleRepository.delete(rulesNeedToDel);
			}
		}
	}

}
