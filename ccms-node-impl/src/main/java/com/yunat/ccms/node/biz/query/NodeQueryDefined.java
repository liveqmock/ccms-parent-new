package com.yunat.ccms.node.biz.query;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.yunat.ccms.metadata.pojo.Query;

/**
 * 实体类：查询节点
 * 
 * @author kevin.jiang 2013-3-18
 */
@Entity
@Table(name = "twf_node_query_defined")
public class NodeQueryDefined implements Serializable {

	private static final long serialVersionUID = -6920520947966355982L;

	private Long id;
	private NodeQuery nodeQuery;
	private Query query;
	private String relation;
	private String extCtrlInfo;
	private Set<NodeQueryCriteria> criterias = new HashSet<NodeQueryCriteria>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "query_defined_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "node_id")
	public NodeQuery getNodeQuery() {
		return nodeQuery;
	}

	public void setNodeQuery(NodeQuery nodeQuery) {
		this.nodeQuery = nodeQuery;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "query_id")
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "query_defined_id")
	public Set<NodeQueryCriteria> getCriterias() {
		return criterias;
	}

	public void setCriterias(Set<NodeQueryCriteria> criterias) {
		this.criterias = criterias;
	}

	@Column(name = "relation")
	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Column(name = "ext_ctrl_info")
	public String getExtCtrlInfo() {
		return extCtrlInfo;
	}

	public void setExtCtrlInfo(String extCtrlInfo) {
		this.extCtrlInfo = extCtrlInfo;
	}
}
