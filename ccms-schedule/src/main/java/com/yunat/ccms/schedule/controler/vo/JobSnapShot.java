package com.yunat.ccms.schedule.controler.vo;

import java.util.List;

/**
 * job状态快照
 * 
 * @author xiaojing.qu
 * 
 */
public class JobSnapShot {

	private Long jobId;
	private String startTime;
	private Long jobStatus;
	private Boolean isTest;
	private List<NodeSnapShot> nodeStatusList;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Long getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(Long jobStatus) {
		this.jobStatus = jobStatus;
	}

	public List<NodeSnapShot> getNodeStatusList() {
		return nodeStatusList;
	}

	public void setNodeStatusList(List<NodeSnapShot> nodeStatusList) {
		this.nodeStatusList = nodeStatusList;
	}

	public Boolean getIsTest() {
		return isTest;
	}

	public void setIsTest(Boolean isTest) {
		this.isTest = isTest;
	}

}
