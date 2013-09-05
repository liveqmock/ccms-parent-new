package com.yunat.ccms.node.biz.query;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.yunat.ccms.metadata.pojo.QueryCriteria;

/**
 * 实体类：查询节点自定义条件
 * 
 * @author kevin.jiang 2013-3-18
 */
@Entity
@Table(name = "twf_node_query_criteria")
public class NodeQueryCriteria implements Serializable {

	private static final long serialVersionUID = -5689816313550473196L;

	private Long id;
	private NodeQueryDefined nodeQueryDefined;
	private QueryCriteria queryCriteria;
	private String uiCode; // 消费查询等固定界面，需要能够快速识别条件对照界面上哪个条件，需要用uiCode去查找
	private String operator;
	private String targetValue;
	private String subGroup;
	private String relation;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "node_criteria_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "query_defined_id")
	public NodeQueryDefined getNodeQueryDefined() {
		return nodeQueryDefined;
	}

	public void setNodeQueryDefined(NodeQueryDefined nodeQueryDefined) {
		this.nodeQueryDefined = nodeQueryDefined;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "query_criteria_id")
	public QueryCriteria getQueryCriteria() {
		return queryCriteria;
	}

	public void setQueryCriteria(QueryCriteria queryCriteria) {
		this.queryCriteria = queryCriteria;
	}

	@Column(name = "target_value", length = 200)
	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	@Column(name = "operator", length = 32)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "sub_group", length = 32)
	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	@Column(name = "relation", length = 3)
	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Column(name = "ui_code", length = 32)
	public String getUiCode() {
		return uiCode;
	}

	public void setUiCode(String uiCode) {
		this.uiCode = uiCode;
	}
}
