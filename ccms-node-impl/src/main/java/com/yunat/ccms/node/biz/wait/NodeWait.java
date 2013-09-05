package com.yunat.ccms.node.biz.wait;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yunat.ccms.core.support.annotation.Descriptor;
import com.yunat.ccms.node.spi.NodeEntity;

/**
 * 等待节点配置信息
 * 
 * 
 */
@Entity
@Table(name = "twf_node_wait")
@Descriptor(type = NodeWait.TYPE, 
		validatorClass = com.yunat.ccms.node.biz.wait.NodeWaitValidator.class,
		handlerClass = com.yunat.ccms.node.biz.wait.NodeWaitHandler.class,
		processorClass = com.yunat.ccms.node.biz.wait.NodeWaitProcessor.class)
public class NodeWait implements java.io.Serializable, NodeEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 2617785428219337410L;

	/** 等待 */
	public static final String TYPE = "tflowwait";

	/**
	 * 节点ID
	 */
	private Long id;

	private String name;

	/**
	 * 0表示等待到指定日期 1 表示到等待天数 2表示等待时间（小时，分钟）
	 */
	private Long isDate;

	/**
	 * 等待到指定日期
	 */
	private Date waitdate;
	/**
	 * 等待到指定天数
	 */
	private Long waitday;
	/**
	 * 时间点
	 */
	private String waittime;

	/** 等待小时数 */
	private Integer waithour;

	/** 等待分钟数 */
	private Integer waitminute;

	// Constructors

	/** default constructor */
	public NodeWait() {
	}

	/** minimal constructor */
	public NodeWait(Long id) {
		this.id = id;
	}

	/** full constructor */
	public NodeWait(Long camId, Date waitdate, Long waitday, String waittime) {
		this.id = camId;
		this.waitdate = waitdate;
		this.waitday = waitday;
		this.waittime = waittime;
	}

	// Property accessors

	@Id
	@Column(name = "node_id", precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "isdate", length = 10)
	public Long getIsDate() {
		return isDate;
	}

	public void setIsDate(Long isDate) {
		this.isDate = isDate;
	}

	@Column(name = "wait_date", length = 7)
	public Date getWaitdate() {
		return this.waitdate;
	}

	public void setWaitdate(Date waitdate) {
		this.waitdate = waitdate;
	}

	@Column(name = "wait_day", precision = 10, scale = 0)
	public Long getWaitday() {
		return this.waitday;
	}

	public void setWaitday(Long waitday) {
		this.waitday = waitday;
	}

	@Column(name = "wait_time", length = 10)
	public String getWaittime() {
		return this.waittime;
	}

	public void setWaittime(String waittime) {
		this.waittime = waittime;
	}

	@Column(name = "node_name", length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "wait_hour", precision = 5, scale = 0)
	public Integer getWaithour() {
		return waithour;
	}

	public void setWaithour(Integer waithour) {
		this.waithour = waithour;
	}

	@Column(name = "wait_minute", precision = 5, scale = 0)
	public Integer getWaitminute() {
		return waitminute;
	}

	public void setWaitminute(Integer waitminute) {
		this.waitminute = waitminute;
	}

	public String toString() {
		return "id" + id + ";name" + name + ";isDate" + isDate + ";waitdate" + waitdate + ";waitday" + waitday
				+ ";waittime" + waittime + ";waithour" + waithour + ";waitminute" + waitminute;
	}

}