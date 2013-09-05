package com.yunat.ccms.rule.center.conf.condition;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.yunat.ccms.rule.center.conf.BaseVO;
import com.yunat.ccms.rule.center.conf.HasPosition;
import com.yunat.ccms.rule.center.conf.rule.Rule;

/**
 * 条件. 条件是规则的组成部分. 一个条件的规则是 指标 指标比较符 参考值 参见:规则{@link Rule},指标比较符 {@link ConditionOp}
 * 
 * @author wenjian.liang
 */
@Entity
@Table(name = "rc_condition")
public class Condition extends BaseVO implements HasPosition, Comparable<Condition> {

	private static final long serialVersionUID = 8858975760411221108L;

	@Column
	private String name;

	/**
	 * 所属于的规则id
	 */
	@Column(name = "rule_id")
	private long ruleId;

	/**
	 * 优先级顺序,从1开始
	 */
	@Column
	private int position;

	/**
	 * 与其他条件之间的关系
	 */
	@Column
	private String relation = "AND";

	/**
	 * 类型,参见{@link ConditionType}
	 */
	@Column
	private String type;

	/**
	 * 指标选择
	 * ref to DatabaseColumn
	 */
	@Column(name = "property_id")
	private Long propertyId;

	/**
	 * 指标比较符
	 */
	@Column(name = "condition_op_name")
	private String conditionOpName;

	/**
	 * 参考值
	 */
	@Column(name = "reference_value")
	private String referenceValue;

	/**
	 * 最后配置时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_config_time")
	private Date lastConfigTime;

	@Column(name = "use_default_name")
	private boolean useDefaultName = true;

	/**
	 * 陶勇 说：“修改条件的弹框不能在其弹出后才请求指标类型等数据，否则有前端循环弹框之类的问题。”
	 * 为此，需要后端在返回Condition时加一个字段标识它应该是input还是select。
	 */
	@Transient
	private boolean hasProvidedValues = false;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public long getRuleId() {
		return ruleId;
	}

	public void setRuleId(final long ruleId) {
		this.ruleId = ruleId;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public void setPosition(final int position) {
		this.position = position;
	}

	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(final String referenceValue) {
		this.referenceValue = referenceValue;
	}

	public String getConditionOpName() {
		return conditionOpName;
	}

	public void setConditionOpName(final String conditionOpName) {
		this.conditionOpName = conditionOpName;
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(final Long propertyId) {
		this.propertyId = propertyId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(final String relation) {
		this.relation = relation;
	}

	public Date getLastConfigTime() {
		return lastConfigTime;
	}

	public void setLastConfigTime(final Date lastConfigTime) {
		this.lastConfigTime = lastConfigTime;
	}

	@Override
	public int compareTo(final Condition o) {
		return position - o.position;
	}

	public boolean getUseDefaultName() {
		return useDefaultName;
	}

	public void setUseDefaultName(final boolean useDefaultName) {
		this.useDefaultName = useDefaultName;
	}

	public boolean getHasProvidedValues() {
		return hasProvidedValues;
	}

	public void setHasProvidedValues(final boolean hasProvidedValues) {
		this.hasProvidedValues = hasProvidedValues;
	}
}
