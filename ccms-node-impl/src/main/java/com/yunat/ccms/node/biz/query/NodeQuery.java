package com.yunat.ccms.node.biz.query;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.yunat.ccms.core.support.annotation.Descriptor;
import com.yunat.ccms.node.spi.NodeEntity;

/**
 * 实体类：查询节点
 * 
 * @author kevin.jiang 2013-3-18
 */
@Entity
@Table(name = "twf_node_query")
@Descriptor(type = NodeQuery.TYPE, 
	validatorClass = com.yunat.ccms.node.biz.query.NodeQueryValidator.class, 
	handlerClass = com.yunat.ccms.node.biz.query.NodeQueryHandler.class, 
	processorClass = com.yunat.ccms.node.biz.query.QueryProcessor.class)
public class NodeQuery implements Serializable, NodeEntity {

	private static final long serialVersionUID = 7653268348488202253L;

	public static final String TYPE = "tfilterfind";

	private Long nodeId;
	private Boolean isExclude;
	private Integer timeType;
	private String platCode;
	Set<NodeQueryDefined> queryDefineds = new LinkedHashSet<NodeQueryDefined>();

	@Id
	@Column(name = "node_id", unique = true, nullable = false)
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "is_exclude")
	public Boolean getIsExclude() {
		return isExclude;
	}

	public void setIsExclude(Boolean isExclude) {
		this.isExclude = isExclude;
	}

	@Column(name = "time_type")
	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "node_id")
	public Set<NodeQueryDefined> getQueryDefineds() {
		return queryDefineds;
	}

	public void setQueryDefineds(Set<NodeQueryDefined> queryDefineds) {
		this.queryDefineds = queryDefineds;
	}

	@Column(name = "plat_code", length = 32)
	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

}
