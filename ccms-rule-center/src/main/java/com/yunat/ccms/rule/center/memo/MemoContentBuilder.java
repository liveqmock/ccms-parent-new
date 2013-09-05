package com.yunat.ccms.rule.center.memo;

import java.util.Collection;
import java.util.Set;

import com.yunat.ccms.rule.center.conf.planGroup.PlanGroup;
import com.yunat.ccms.rule.center.conf.rule.Rule;

/**
 * 备注生成器
 * 
 * @author wenjian.liang
 */
public interface MemoContentBuilder {

	/**
	 * 生成备注
	 * 
	 * @param shopId
	 * @param matchedRules
	 * @return
	 */
	String buildMemoContent(String shopId, Set<Long> matchedRules);

	String buildMemoContent(PlanGroup planGroup, Collection<Rule> matchedRules);

	String getLastMemo(String shopId, Long tid);
}
