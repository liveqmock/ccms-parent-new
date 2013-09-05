package com.yunat.ccms.channel.support.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "twf_node_retry")
public class NodeRetry implements Serializable {

	private static final long serialVersionUID = -669917370664542263L;

	private Long id;
	private Long jobId;
	private Long nodeId;
	private Boolean isTestExecute;
	private String failedReason;
	private String failedCode;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "job_id")
	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(name = "node_id")
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "is_test_execute", columnDefinition = "BOOLEAN")
	public Boolean getIsTestExecute() {
		return isTestExecute;
	}

	public void setIsTestExecute(Boolean isTestExecute) {
		this.isTestExecute = isTestExecute;
	}

	@Column(name = "failed_reason")
	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	@Column(name = "failed_code")
	public String getFailedCode() {
		return failedCode;
	}

	public void setFailedCode(String failedCode) {
		this.failedCode = failedCode;
	}

}