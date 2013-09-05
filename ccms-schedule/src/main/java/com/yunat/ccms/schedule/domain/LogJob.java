package com.yunat.ccms.schedule.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.yunat.ccms.core.support.statemachine.state.JobState;

/**
 * 活动运行一次产生的日志
 */
@Entity
@Table(name = "TWF_LOG_JOB")
public class LogJob implements java.io.Serializable {

	private static final long serialVersionUID = -5976952829554732422L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "persistenceGenerator", strategy = "identity")
	@Column(name = "JOB_ID")
	private Long jobId;

	@Column(name = "CAMP_ID")
	private Long campId;

	@Column(name = "STATUS")
	private Long status;

	@Column(name = "PLANTIME")
	private Date plantime;

	@Column(name = "STARTTIME")
	private Date starttime;

	@Column(name = "ENDTIME")
	private Date endtime;

	@Column(name = "IS_TEST")
	private Boolean isTest;

	@Column(name = "LAST_JOB")
	private Boolean lastJob;

	// Constructors

	/** default constructor */
	public LogJob() {
	}

	/** 创建一个新的logjob */
	public LogJob(Long campId, boolean isTest, Date plantime, boolean isLastJob) {
		this.campId = campId;
		this.plantime = plantime;
		this.isTest = isTest;
		this.lastJob = isLastJob;
		this.status = JobState.INITIAL.getCode();

	}

	// Property accessors

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

	public Long getStatus() {
		return this.status;
	}

	public JobState getState() {
		return JobState.fromCode(this.status);
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getPlantime() {
		return plantime;
	}

	public void setPlantime(Date plantime) {
		this.plantime = plantime;
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

	/**
	 * 是否是活动的最后一个job（对正式执行有意义）
	 * 
	 * @return
	 */
	public Boolean isLastJob() {
		return lastJob;
	}

	public Boolean getLastJob() {
		return lastJob;
	}

	public void setLastJob(Boolean lastJob) {
		this.lastJob = lastJob;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
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
		LogJob other = (LogJob) obj;
		if (jobId == null) {
			if (other.jobId != null)
				return false;
		} else if (!jobId.equals(other.jobId))
			return false;
		return true;
	}

}
