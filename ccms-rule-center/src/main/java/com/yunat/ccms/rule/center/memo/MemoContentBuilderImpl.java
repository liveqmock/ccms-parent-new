package com.yunat.ccms.rule.center.memo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradeGetRequest;
import com.taobao.api.response.TradeGetResponse;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.channel.external.taobao.handler.CommonInvokerHandler;
import com.yunat.ccms.rule.center.RuleCenterCons;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroup;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroupService;
import com.yunat.ccms.rule.center.conf.rule.Rule;
import com.yunat.ccms.rule.center.conf.rule.RuleService;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

/**
 * @author wenjian.liang
 */
@Component
public class MemoContentBuilderImpl implements MemoContentBuilder {

	@Autowired
	private PlanGroupService planGroupService;

	@Autowired
	private RuleService ruleService;

	@Autowired
	private CommonInvokerHandler invokerHandler;

	@Autowired
	private AccessTokenService accessTokenService;

	@Override
	public String buildMemoContent(String shopId, Set<Long> matchedRules) {
		PlanGroup planGroup = planGroupService.planGroupOfShop(shopId);
		List<Rule> allMatchedRules = ruleService.getOrderedRule(matchedRules);
		return buildMemoContent(planGroup, allMatchedRules);
	}

	@Override
	public String buildMemoContent(PlanGroup planGroup, Collection<Rule> matchedRules) {
		List<String> allRuleMemos = new ArrayList<String>();
		for (final Rule rule : matchedRules) {
			if (rule != null && !StringUtils.isBlank(rule.getRemarkContent())) {
				allRuleMemos.add(rule.getRemarkContent());
			}
		}
		String joinedMemos = StringUtils.join(allRuleMemos, RuleCenterCons.REMARK_CONTENT_SEPERATOR);
		String result = joinedMemos + "【" + planGroup.getSign() + "】";
		return result;
	}

	@Override
	public String getLastMemo(String shopId, Long tid) {
		final AccessToken accessToken = accessTokenService.getAccessToken(PlatEnum.taobao, shopId);
		TradeGetRequest req = new TradeGetRequest();
		req.setFields("seller_memo");
		req.setTid(tid);
		TradeGetResponse resp = invokerHandler.execute(req, accessToken.getAccessToken());
		Trade trade = resp.getTrade();
		if (trade != null) {
			return trade.getSellerMemo();
		}
		return null;
	}
}
