package com.yunat.ccms.schedule.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.yunat.ccms.core.support.statemachine.state.SubjobState;
import com.yunat.ccms.workflow.domain.Node;

/**
 * 活动中节点运行一次产生的日志
 */
@Entity
@Table(name = "TWF_LOG_SUBJOB")
public class LogSubjob implements java.io.Serializable {

	private static final long serialVersionUID = 3599855570906838238L;

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "persistenceGenerator", strategy = "identity")
	@Column(name = "SUBJOB_ID", precision = 10, scale = 0)
	private Long subjobId;

	@Column(name = "JOB_ID")
	private Long jobId;

	@Column(name = "CAMP_ID")
	private Long campId;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "NODE_ID")
	private Node node;

	@Column(name = "STATUS")
	private Long status;

	@Column(name = "STARTTIME")
	private Date starttime;

	@Column(name = "ENDTIME")
	private Date endtime;

	/**
	 * 计划执行时间，add by quxiaojing
	 */
	@Column(name = "PLANTIME")
	private Date plantime;

	@Column(name = "MEMO")
	private String memo;

	@Column(name = "OUTPUT_MSG")
	private String outputMsg;

	@Column(name = "IS_TEST")
	private Boolean isTest;

	// Constructors

	/** default constructor */
	public LogSubjob() {
	}

	// Property accessors

	public Long getSubjobId() {
		return this.subjobId;
	}

	public void setSubjobId(Long subjobId) {
		this.subjobId = subjobId;
	}

	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Long getStatus() {
		return this.status;
	}

	public SubjobState getState() {
		return SubjobState.fromCode(this.status);
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public String getOutputMsg() {
		return outputMsg;
	}

	public void setOutputMsg(String outputMsg) {
		this.outputMsg = outputMsg;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 是否是测试执行
	 * 
	 * @return
	 */
	public Boolean isTest() {
		return isTest;
	}

	public Boolean getIsTest() {
		return isTest;
	}

	public void setIsTest(Boolean isTest) {
		this.isTest = isTest;
	}

	public Date getPlantime() {
		return plantime;
	}

	public void setPlantime(Date plantime) {
		this.plantime = plantime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subjobId == null) ? 0 : subjobId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogSubjob other = (LogSubjob) obj;
		if (subjobId == null) {
			if (other.subjobId != null)
				return false;
		} else if (!subjobId.equals(other.subjobId))
			return false;
		return true;
	}

}