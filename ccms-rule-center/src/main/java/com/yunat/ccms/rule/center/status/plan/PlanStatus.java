package com.yunat.ccms.rule.center.status.plan;

import java.util.Date;
import java.util.List;

public class PlanStatus {

	private List<RuleStatus> ruleData;
	private long handled;
	private long matched;

	private Date from;
	private Date to;

	public List<RuleStatus> getRuleData() {
		return ruleData;
	}

	public void setRuleData(final List<RuleStatus> ruleData) {
		this.ruleData = ruleData;
	}

	public long getHandled() {
		return handled;
	}

	public void setHandled(final long handled) {
		this.handled = handled;
	}

	public long getMatched() {
		return matched;
	}

	public void setMatched(final long matched) {
		this.matched = matched;
	}

	@Override
	public String toString() {
		return "PlanStatus [ruleData=" + ruleData + ", handled=" + handled + ", matched=" + matched + "]";
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(final Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(final Date to) {
		this.to = to;
	}
}
