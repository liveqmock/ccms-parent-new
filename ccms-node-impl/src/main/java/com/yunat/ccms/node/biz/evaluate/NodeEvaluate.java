package com.yunat.ccms.node.biz.evaluate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.annotation.Descriptor;
import com.yunat.ccms.core.support.json.JsonDateSerializer;
import com.yunat.ccms.node.spi.NodeEntity;

/**
 * 效果评估节点实体
 * 
 * @author yin
 * 
 */

@Entity
@Table(name = "twf_node_evaluate")
@Descriptor(type = NodeEvaluate.TYPE, 
	validatorClass = com.yunat.ccms.node.biz.evaluate.NodeEvaluateValidator.class, 
	handlerClass = com.yunat.ccms.node.biz.evaluate.NodeEvaluateHandler.class, 
	processorClass = com.yunat.ccms.node.biz.evaluate.NodeEvaluateProcess.class)
public class NodeEvaluate implements Serializable, NodeEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8203574914633838975L;

	public static final String TYPE = "tcustomerevaluate";

	// 评估节点ID
	private Long nodeId;

	// 评估节点名称
	private String nodeName;

	// 评估节点周期(1~7天 )
	private Integer evaluateCycle;

	// 评估店铺
	private String shopId;

	// 创建时间
	private Date created;

	public NodeEvaluate() {
	}

	public NodeEvaluate(Long nodeId, String nodeName, Integer evaluateCycle, String shopId, Date created) {
		setNodeId(nodeId);
		setNodeName(nodeName);
		setEvaluateCycle(evaluateCycle);
		setShopId(shopId);
		setCreated(created);
	}

	@Id
	@Column(name = "node_id", unique = true, nullable = false)
	@NotNull
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "node_name")
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Column(name = "evaluate_cycle")
	public Integer getEvaluateCycle() {
		return evaluateCycle;
	}

	public void setEvaluateCycle(Integer evaluateCycle) {
		this.evaluateCycle = evaluateCycle;
	}

	/*
	 * @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	 * 
	 * @JoinColumn(name = "shop_id")
	 */

	@Column(name = "shop_id")
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	@Column(name = "created")
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.nodeId != null ? this.nodeId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the nodeId fields
		// are not set
		if (!(object instanceof NodeEvaluate)) {
			return false;
		}
		NodeEvaluate other = (NodeEvaluate) object;
		if (this.nodeId != other.nodeId && (this.nodeId == null || !this.nodeId.equals(other.nodeId)))
			return false;
		return true;
	}

}
