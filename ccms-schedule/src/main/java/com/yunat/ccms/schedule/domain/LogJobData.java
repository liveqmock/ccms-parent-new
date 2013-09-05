package com.yunat.ccms.schedule.domain;

import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.support.io.Empty;
import com.yunat.ccms.node.support.io.Table;
import com.yunat.ccms.node.support.io.View;

public class LogJobData {

	private Long campId;

	private Long jobId;

	private Long subjobId;

	private Long source;

	private Long target;

	private String dataType;

	private String dataCode;

	public LogJobData() {
	}

	public LogJobData(Long campId, Long jobId, Long subjobId, Long source) {
		this.campId = campId;
		this.jobId = jobId;
		this.subjobId = subjobId;
		this.source = source;
	}

	public NodeData toNodeData() {
		if (Table.class.getSimpleName().equals(dataType)) {
			return new Table(dataCode);
		} else if (View.class.getSimpleName().equals(dataType)) {
			return new View(dataCode);
		} else if (Empty.class.getSimpleName().equals(dataType)) {
			return new Empty();
		} else {
			return null;
		}

	}

	public Long getSubjobId() {
		return subjobId;
	}

	public void setSubjobId(Long subjobId) {
		this.subjobId = subjobId;
	}

	public Long getJobId() {
		return jobId;
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

	public Long getSource() {
		return source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	public Long getTarget() {
		return target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

}
