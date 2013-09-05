package com.yunat.ccms.workflow.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 该类是用来存储一个流程的结构，包含Node和Connect
 */
@Entity
@Table(name = "twf_workflow")
public class WorkFlow implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4642757051487752084L;

	/**
	 * 流程ID
	 */
	private Long workflowId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 最后更新时间
	 */
	private Date updateTime;

	/**
	 * 所有节点
	 */
	private Set<Node> allNodes;

	/**
	 * 所有节点之间的连接
	 */
	private Set<Connect> allConnects;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "workflow_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "workflowId", fetch = FetchType.EAGER)
	public Set<Node> getAllNodes() {
		return allNodes;
	}

	public void setAllNodes(Set<Node> allNodes) {
		this.allNodes = allNodes;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "workflowId", fetch = FetchType.EAGER)
	public Set<Connect> getAllConnects() {
		return allConnects;
	}

	public void setAllConnects(Set<Connect> allConnects) {
		this.allConnects = allConnects;
	}
}