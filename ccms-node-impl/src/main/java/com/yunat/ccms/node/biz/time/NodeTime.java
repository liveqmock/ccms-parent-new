package com.yunat.ccms.node.biz.time;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.yunat.ccms.core.support.annotation.Descriptor;
import com.yunat.ccms.node.spi.NodeEntity;

/**
 * 时间节点的配置信息
 * 
 */
@Entity
@Table(name = "twf_node_time")
@Descriptor(type = NodeTime.TYPE, hasCountLog = false, validatorClass = com.yunat.ccms.node.biz.time.TimeValidator.class, handlerClass = com.yunat.ccms.node.biz.time.NodeTimeHandler.class, processorClass = com.yunat.ccms.node.biz.time.NodeTimeProcessor.class)
public class NodeTime implements java.io.Serializable, NodeEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = 3899101139308868007L;

	/** 时间 */
	public static final String TYPE = "tflowtime";

	public static final String CYCLE_TYPE_HOUR = "hour";
	public static final String CYCLE_TYPE_DAY = "day";
	public static final String CYCLE_TYPE_WEEK = "week";
	public static final String CYCLE_TYPE_MONTH = "month";

	/**
	 * 所属活动的节点ID
	 */
	private Long id;

	/**
	 * 活动节点名称
	 */
	private String name;

	/**
	 * 是否是周期性 0，不是，1 是
	 */
	private Long iscycle;

	/**
	 * 是否即时执行 0，不是，1 是
	 */
	private Long isrealtime;

	/**
	 * 开始日期
	 */
	private Date realtimebeginDate;

	/**
	 * 开始时间， 小时和分钟按':'分隔
	 */
	private String realtimebeginTime;

	/**
	 * 开始日期2 for 周期性的
	 */
	private Date cyclebeginDate;

	/**
	 * 结束时间2 for 周期性的
	 */
	private Date cycleendDate;

	/**
	 * 开始时间2 for 周期性的
	 */
	private String cyclebeginTime;

	/**
	 * 周期类型： day 每天 week 每周 month 每月
	 */
	private String cycleType;

	/**
	 * 周期值
	 */
	private String cycleValue;

	// 每周选择的日期 以逗号分割
	private String weekCycleValue;

	// 每月选择的日期 以逗号分割
	private String monthCycleValue;

	/** default constructor */
	public NodeTime() {
	}

	@Id
	@Column(name = "node_id", precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "iscycle", precision = 1, scale = 0, nullable = false)
	public Long getIscycle() {
		return this.iscycle;
	}

	public void setIscycle(Long iscycle) {
		this.iscycle = iscycle;
	}

	@Column(name = "isrealtime", precision = 1, scale = 0)
	public Long getIsrealtime() {
		return this.isrealtime;
	}

	public void setIsrealtime(Long isrealtime) {
		this.isrealtime = isrealtime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "realtime_begin_date")
	public Date getRealtimebeginDate() {
		return realtimebeginDate;
	}

	public void setRealtimebeginDate(Date realtimebeginDate) {
		this.realtimebeginDate = realtimebeginDate;
	}

	@Column(name = "realtime_begin_time", length = 10)
	public String getRealtimebeginTime() {
		return realtimebeginTime;
	}

	public void setRealtimebeginTime(String realtimebeginTime) {
		this.realtimebeginTime = realtimebeginTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "cycle_begin_date")
	public Date getCyclebeginDate() {
		return cyclebeginDate;
	}

	public void setCyclebeginDate(Date cyclebeginDate) {
		this.cyclebeginDate = cyclebeginDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "cycle_end_date")
	public Date getCycleendDate() {
		return cycleendDate;
	}

	public void setCycleendDate(Date cycleendDate) {
		this.cycleendDate = cycleendDate;
	}

	@Column(name = "cycle_begin_time", length = 10)
	public String getCyclebeginTime() {
		return cyclebeginTime;
	}

	public void setCyclebeginTime(String cyclebeginTime) {
		this.cyclebeginTime = cyclebeginTime;
	}

	@Column(name = "cycle_type", length = 10)
	public String getCycleType() {
		return this.cycleType;
	}

	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}

	@Column(name = "cycle_value", length = 100)
	public String getCycleValue() {
		return this.cycleValue;
	}

	public void setCycleValue(String cycleValue) {
		this.cycleValue = cycleValue;
	}

	@Transient
	public String getWeekCycleValue() {
		return weekCycleValue;
	}

	public void setWeekCycleValue(String weekCycleValue) {
		this.weekCycleValue = weekCycleValue;
	}

	@Transient
	public String getMonthCycleValue() {
		return monthCycleValue;
	}

	public void setMonthCycleValue(String monthCycleValue) {
		this.monthCycleValue = monthCycleValue;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the nodeId fields
		// are not set
		if (!(object instanceof NodeTime)) {
			return false;
		}
		NodeTime other = (NodeTime) object;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
			return false;
		return true;
	}

}