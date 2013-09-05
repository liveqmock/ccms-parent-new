package com.yunat.ccms.rule.center.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 规则引擎处理后的结果
 * 
 * @author xiaojing.qu
 * 
 */
public final class FactResult {

	private static Logger logger = LoggerFactory.getLogger(FactResult.class);

	/*** 总共匹配的规则数量 */
	private int hits = 0;
	/*** 总共匹配的规则详情 */
	private List<RuleHitDetail> hitDetails = new ArrayList<RuleHitDetail>();

	public void hit(long planId, long ruleId) {
		RuleHitDetail detail = new RuleHitDetail(planId, ruleId);
		logger.info(detail.toString());
		hitDetails.add(detail);
		hits++;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public List<RuleHitDetail> getHitDetails() {
		return hitDetails;
	}

	public void setHitDetails(List<RuleHitDetail> hitDetails) {
		this.hitDetails = hitDetails;
	}

	public static class RuleHitDetail {
		private Long planId;
		private Long ruleId;
		private Date timestamp = new Date();

		public RuleHitDetail(Long planId, Long ruleId) {
			this.planId = planId;
			this.ruleId = ruleId;
		}

		public Long getPlanId() {
			return planId;
		}

		public void setPlanId(Long planId) {
			this.planId = planId;
		}

		public Long getRuleId() {
			return ruleId;
		}

		public void setRuleId(Long ruleId) {
			this.ruleId = ruleId;
		}

		public Date getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}

		@Override
		public String toString() {
			return "RuleHitDetail [planId=" + planId + ", ruleId=" + ruleId + ", timestamp=" + timestamp + "]";
		}
	}

}
