package com.yunat.ccms.workflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 该类是用来存储mxGraph的xml中关于连接的数据的， 基本上属性都和mxGraph的存储的xml结构相同
 */
@Entity
@Table(name = "twf_connect")
public class Connect implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9086864618813997355L;

	private Long id;
	private String edge;
	private Long workflowId;
	private Long source;
	private Node sourceNode;
	private Long target;
	private Node targetNode;
	private String relative;
	private String asT;

	/** default constructor */
	public Connect() {
	}

	/** minimal constructor */
	public Connect(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Connect(Long id, String edge, Long workflowId, Long source,
			Long target, String relative, String asT) {
		this.id = id;
		this.edge = edge;
		this.workflowId = workflowId;
		this.source = source;
		this.target = target;
		this.relative = relative;
		this.asT = asT;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "connect_id", precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "edge", length = 20)
	public String getEdge() {
		return this.edge;
	}

	public void setEdge(String edge) {
		this.edge = edge;
	}

	@Column(name = "workflow_id", precision = 10, scale = 0)
	public Long getWorkflowId() {
		return this.workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	@Column(name = "source", precision = 10, scale = 0)
	public Long getSource() {
		return this.source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	@Column(name = "target", precision = 10, scale = 0)
	public Long getTarget() {
		return this.target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}

	@Column(name = "relative", length = 20)
	public String getRelative() {
		return this.relative;
	}

	public void setRelative(String relative) {
		this.relative = relative;
	}

	@Column(name = "as_t", length = 10)
	public String getAsT() {
		return this.asT;
	}

	public void setAsT(String asT) {
		this.asT = asT;
	}

	@ManyToOne
	@JoinColumn(name = "source", updatable = false, insertable = false)
	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	@ManyToOne
	@JoinColumn(name = "target", updatable = false, insertable = false)
	public Node getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Connect other = (Connect) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Connect [workflowId=" + workflowId + ", source=" + source
				+ ", target=" + target + "]";
	}
}