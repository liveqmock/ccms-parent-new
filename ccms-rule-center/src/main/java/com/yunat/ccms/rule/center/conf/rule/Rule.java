package com.yunat.ccms.rule.center.conf.rule;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.yunat.ccms.rule.center.conf.BaseVO;
import com.yunat.ccms.rule.center.conf.HasPosition;
import com.yunat.ccms.rule.center.conf.condition.Condition;
import com.yunat.ccms.rule.center.conf.plan.Plan;

/**
 * 规则.
 * 规则是方案的组成部分.规则之间是互斥的,即一个方案只有第一个匹配的规则生效
 * 参见:方案{@link Plan},条件{@link Condition}
 * 
 * @author wenjian.liang
 */
@Entity
@Table(name = "rc_rule")
public class Rule extends BaseVO implements HasPosition, Comparable<Rule> {

	private static final long serialVersionUID = -2884079238305727244L;

	@Column
	private String name;

	/**
	 * 在所属方案中的优先级位置,从1开始
	 */
	@Column
	private int position;

	/**
	 * 要在备注上添加的内容
	 */
	@Column(name = "remark_content")
	private String remarkContent;

	/**
	 * 所属于的方案的id
	 */
	@Column(name = "plan_id")
	private long planId;

	/**
	 * 最后配置时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_config_time")
	private Date lastConfigTime;

	/**
	 * 条件列表
	 */
	@Transient
	private List<Condition> conditions;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(final List<Condition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public void setPosition(final int index) {
		position = index;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(final long planId) {
		this.planId = planId;
	}

	public String getRemarkContent() {
		return remarkContent;
	}

	public void setRemarkContent(final String remarkContent) {
		this.remarkContent = remarkContent;
	}

	public Date getLastConfigTime() {
		return lastConfigTime;
	}

	public void setLastConfigTime(final Date lastConfigTime) {
		this.lastConfigTime = lastConfigTime;
	}

	@Override
	public int compareTo(final Rule o) {
		return position - o.position;
	}
}
