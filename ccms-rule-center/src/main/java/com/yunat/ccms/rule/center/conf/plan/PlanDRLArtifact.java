package com.yunat.ccms.rule.center.conf.plan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.ccms.rule.center.conf.rule.Rule;
import com.yunat.ccms.rule.center.drl.DRLFragment;
import com.yunat.ccms.rule.center.drl.DRLFragmentBuilder;
import com.yunat.ccms.rule.center.drl.RuleFragment;
import com.yunat.ccms.rule.center.drl.RuleFragmentBuilder;
import com.yunat.ccms.rule.center.drl.convert.DateConverter;
import com.yunat.ccms.rule.center.drl.convert.LocationConverter;
import com.yunat.ccms.rule.center.runtime.fact.Customer;
import com.yunat.ccms.rule.center.runtime.fact.Order;

@Component
public class PlanDRLArtifact implements DRLFragmentBuilder<Plan> {

	@Autowired
	private RuleFragmentBuilder<Rule> ruleStatementArtifact;

	@Override
	public DRLFragment toDRL(final Plan obj) {
		//注意:不要重构包路径!!!!!!会使开始时生成的drl文件出错!!!等以后改为"服务器启动时重新生成一遍drl文件"就好了
		final DRLFragment drl = new DRLFragment();
		drl.setPackageRegion(Plan.class.getName() + obj.getId());
		final List<String> importRegion = Lists.newArrayList();
		importRegion.add(Order.class.getName());
		importRegion.add(Customer.class.getName());
		importRegion.add(DateConverter.class.getName());
		importRegion.add(LocationConverter.class.getName());

		drl.setImportRegion(importRegion);

		final List<Rule> rules = obj.getRules();
		final List<RuleFragment> ruleFragmentList = Lists.newArrayList();
		for (final Rule rule : rules) {
			ruleFragmentList.add(ruleStatementArtifact.toRule(rule));
		}
		drl.setRules(ruleFragmentList);
		return drl;
	}

}
