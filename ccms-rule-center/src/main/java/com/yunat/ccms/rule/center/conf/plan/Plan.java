package com.yunat.ccms.rule.center.conf.plan;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateSerializer;
import com.yunat.ccms.rule.center.conf.BaseVO;
import com.yunat.ccms.rule.center.conf.HasPosition;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroup;
import com.yunat.ccms.rule.center.conf.rule.Rule;

/**
 * 方案.
 * 一个方案有若干条规则,方案之间是共存的,规则之间是互斥的,即一个方案只有第一个匹配的规则生效
 * 参见:规则{@link Rule},方案组{@link PlanGroup}
 * 
 * @author wenjian.liang
 */
@Entity
@Table(name = "rc_plan")
public class Plan extends BaseVO implements HasPosition, Comparable<Plan> {

	private static final long serialVersionUID = -492371171974734995L;

	@Column
	private String name;
	/**
	 * 在方案组中的优先级顺序,从1开始
	 */
	@Column
	private int position;

	/**
	 * 是否已开启
	 */
	@Column
	private boolean active = false;
	/**
	 * 方案开启时间(其实是最后一次开启时间)
	 */
	@Column(name = "start_time")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date startTime;

	/**
	 * 最后配置时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_config_time")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date lastConfigTime;

	/**
	 * 所属的方案组id
	 */
	@Column(name = "shop_id")
	private String shopId;

	/**
	 * 规则列表
	 */
	@Transient
	private List<Rule> rules;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(final List<Rule> rules) {
		this.rules = rules;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public void setPosition(final int index) {
		position = index;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(final String planGroupId) {
		shopId = planGroupId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	public Date getLastConfigTime() {
		return lastConfigTime;
	}

	public void setLastConfigTime(final Date lastConfigTime) {
		this.lastConfigTime = lastConfigTime;
	}

	@Override
	public int compareTo(final Plan o) {
		return position - o.position;
	}

	@Override
	public String toString() {
		return "Plan [id=" + id + ", active=" + active + ", position=" + position + "]";
	}

}
