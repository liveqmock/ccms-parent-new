package com.yunat.ccms.rule.center.runtime.job;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rc_job_log")
public class RcJobLog {

	@Id
	@Column(name = "tid", nullable = false)
	private String tid;

	/*** 店铺id */
	@Column(name = "shop_id")
	private String shopId;

	/*** 匹配规则数量 */
	@Column(name = "hits")
	private int hits;

	/*** 提交引擎执行fact开始时间 */
	@Column(name = "start_time")
	private Date startTime;

	/*** 引擎执行fact返回时间 */
	@Column(name = "end_time")
	private Date endTime;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "tid")
	private List<RcJobDetailLog> details;

	@Column(name = "count_flag")
	private boolean countFlag = false;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(final String shopId) {
		this.shopId = shopId;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(final int hits) {
		this.hits = hits;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public List<RcJobDetailLog> getDetails() {
		return details;
	}

	public void setDetails(List<RcJobDetailLog> details) {
		this.details = details;
	}

	public boolean getCountFlag() {
		return countFlag;
	}

	public void setCountFlag(boolean countFlag) {
		this.countFlag = countFlag;
	}

	public Set<Long> getMatchedRules() {
		if (details != null) {
			Set<Long> matchedRule = new HashSet<Long>();
			for (RcJobDetailLog detail : details) {
				Long ruleId = detail.getRuleId();
				matchedRule.add(ruleId);
			}
			return matchedRule;
		}
		return null;
	}

	@Override
	public String toString() {
		return "RcJobLog [tid=" + tid + ", shopId=" + shopId + ", hits=" + hits + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", details=" + details + ", countFlag=" + countFlag + "]";
	}

}
