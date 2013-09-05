package com.yunat.ccms.schedule.controler.vo;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.schedule.domain.LogSubjob;

/**
 * 节点状态快照
 * 
 * @author xiaojing.qu
 * 
 */
public class NodeSnapShot {

	private final Long nodeId;
	private final String nodeType;
	private final Long nodeStatus;
	private final String planTime;
	private final String startTime;
	private final String endTime;
	/*** 执行时间 */
	private final String duration;
	private final String outputMsg;
	private final String memo;

	public NodeSnapShot(LogSubjob subjob) {
		this.nodeId = subjob.getNode().getId();
		this.nodeType = subjob.getNode().getType();
		this.nodeStatus = subjob.getStatus();
		this.planTime = subjob.getPlantime() != null ? DateUtils.getStringDate(subjob.getPlantime()) : "";
		this.startTime = subjob.getStarttime() != null ? DateUtils.getStringDate(subjob.getStarttime()) : "";
		this.endTime = subjob.getEndtime() != null ? DateUtils.getStringDate(subjob.getEndtime()) : "";
		this.outputMsg = subjob.getOutputMsg() != null ? subjob.getOutputMsg() : "";
		this.duration = DateUtils.compareDate(subjob.getStarttime(), subjob.getEndtime());
		this.memo = subjob.getMemo() != null ? subjob.getMemo() : "";
	}

	public Long getNodeId() {
		return nodeId;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getNodeType() {
		return nodeType;
	}

	public String getMemo() {
		return memo;
	}

	public String getOutputMsg() {
		return outputMsg;
	}

	public String getDuration() {
		return duration;
	}

	public Long getNodeStatus() {
		return nodeStatus;
	}

	public String getPlanTime() {
		return planTime;
	}

}
