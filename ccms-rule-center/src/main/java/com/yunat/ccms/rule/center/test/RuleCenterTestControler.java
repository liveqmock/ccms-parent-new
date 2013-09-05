package com.yunat.ccms.rule.center.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taobao.api.domain.User;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.rule.center.RuleCenterBusinessException;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroup;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroupService;
import com.yunat.ccms.rule.center.conf.rule.Rule;
import com.yunat.ccms.rule.center.conf.rule.RuleService;
import com.yunat.ccms.rule.center.engine.Fact;
import com.yunat.ccms.rule.center.engine.FactResult;
import com.yunat.ccms.rule.center.engine.FactResult.RuleHitDetail;
import com.yunat.ccms.rule.center.engine.RuleEngineSession;
import com.yunat.ccms.rule.center.engine.RuleEngineSessionRepository;
import com.yunat.ccms.rule.center.memo.MemoContentBuilder;
import com.yunat.ccms.rule.center.runtime.fact.Customer;
import com.yunat.ccms.rule.center.runtime.fact.OrderResolver;

/**
 * 测试规则引擎的REST接口
 * 
 * @author xiaojing.qu
 */
@Controller
@RequestMapping(value = { "/rulecenter/test/*" }, method = RequestMethod.GET)
public class RuleCenterTestControler {

	@Autowired
	private RuleEngineSessionRepository sessionRepository;

	@Autowired
	private OrderResolver factResolver;

	@Autowired
	private PlanGroupService planGroupService;

	@Autowired
	private RuleService ruleService;

	@Autowired
	private MemoContentBuilder remarkContentBuilder;

	/**
	 * 查看给点店铺开启的方案作用于给定订单后可以得到的结果
	 * 
	 * @param shopId
	 * @param tid
	 * @return
	 */
	@RequestMapping(value = "/{shopId}/{tid}")
	@ResponseBody
	public ControlerResult test(@PathVariable("shopId") final String shopId, @PathVariable("tid") final Long tid) {
		try {
			final RuleEngineSession session = sessionRepository.getSession(shopId);
			final Fact fact = factResolver.resolve(tid);
			if (fact != null) {
				session.execute(fact);
				final FactResult result = fact.getResult();
				if (result.getHits() == 0) {
					return ControlerResult.newSuccess("没有匹配的规则！", fact);
				} else {
					final PlanGroup planGroup = planGroupService.planGroupOfShop(shopId);
					final List<Rule> matchedRules = new ArrayList<Rule>();
					for (final RuleHitDetail detail : result.getHitDetails()) {
						final Rule rule = ruleService.getRule(detail.getRuleId());
						matchedRules.add(rule);
					}

					final String remark = remarkContentBuilder.buildMemoContent(planGroup, matchedRules);
					return ControlerResult.newSuccess(remark, fact);
				}

			} else {
				return ControlerResult.newError("获取订单失败！");
			}
		} catch (final RuleCenterBusinessException e) {
			e.printStackTrace();
			return ControlerResult.newError(e.getMessage());
		} catch (final Exception e) {
			e.printStackTrace();
			return ControlerResult.newError(e.getMessage());
		}
	}

	@RequestMapping(value = "/customer/{shopId}/{nick}")
	@ResponseBody
	public ControlerResult customer(@PathVariable("shopId") final String shopId, @PathVariable("nick") final String nick) {
		final Customer customer = factResolver.getCustomer(nick, shopId);
		final User user = factResolver.getTaobaoUserObject(nick, shopId);
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("customer", customer);
		map.put("user", user);
		return ControlerResult.newSuccess(map);
	}
}
