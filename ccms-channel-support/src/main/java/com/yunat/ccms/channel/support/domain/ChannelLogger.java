package com.yunat.ccms.channel.support.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "twf_log_channel")
public class ChannelLogger implements Serializable {
	private static final long serialVersionUID = 6877723975165190361L;

	private Long loggerId;
	private Long subjobId;
	private Long channelId;
	private Date createTime;
	private Date planStartTime;
	private Date planEndTime;
	private Long campaignId;
	private Long nodeId;
	private Long channelType;
	private Boolean testExecute;
	private String taskId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", precision = 22, scale = 0)
	public Long getLoggerId() {
		return loggerId;
	}

	public void setLoggerId(Long loggerId) {
		this.loggerId = loggerId;
	}

	@Column(name = "subjob_id")
	public Long getSubjobId() {
		return subjobId;
	}

	public void setSubjobId(Long subjobId) {
		this.subjobId = subjobId;
	}

	@Column(name = "channel_id")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "plan_start_time")
	public Date getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "plan_end_time")
	public Date getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	@Column(name = "campaign_id")
	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	@Column(name = "node_id")
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "channel_type")
	public Long getChannelType() {
		return channelType;
	}

	public void setChannelType(Long channelType) {
		this.channelType = channelType;
	}

	@Column(name = "is_test_execute")
	public Boolean getTestExecute() {
		return testExecute;
	}

	public void setTestExecute(Boolean testExecute) {
		this.testExecute = testExecute;
	}

	@Column(name = "task_id")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}