package com.yunat.ccms.rule.center.status.plan;

import com.yunat.ccms.rule.center.conf.rule.Rule;

public class RuleStatus {

	private final Rule rule;
	private long matched;
	private String rate;

	public RuleStatus(final Rule rule) {
		super();
		this.rule = rule;
	}

	public long getMatched() {
		return matched;
	}

	public void setMatched(final long matched) {
		this.matched = matched;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(final String rate) {
		this.rate = rate;
	}

	public Long getId() {
		return rule.getId();
	}

	@Override
	public String toString() {
		return "RuleStatus [rule=" + rule + ", matched=" + matched + ", rate=" + rate + "]";
	}

}
