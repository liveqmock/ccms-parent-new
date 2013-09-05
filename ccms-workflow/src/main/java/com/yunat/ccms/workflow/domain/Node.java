package com.yunat.ccms.workflow.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 该类是用来存储mxGraph的xml中关于节点的数据的，
 * 基本上属性都和mxGraph的存储的xml结构相同，但增加了一个type字段，用来表示节点类型，主要是通过style的分割实现
 */
@Entity
@Table(name = "twf_node")
public class Node implements java.io.Serializable {

	private static final long serialVersionUID = -66347530093856993L;

	private Long id;
	private String value;
	private String style;
	private String vertex;
	private Long workflowId;
	private Long x;
	private Long y;
	private Long width;
	private Long height;
	private String asT;
	private String type;
	private String description;

	/** default constructor */
	public Node() {
	}

	/** minimal constructor */
	public Node(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Node(Long id, String value, String style, String vertex,
			Long workflowId, Long x, Long y, Long width, Long height,
			String asT, String type) {
		this.id = id;
		this.value = value;
		this.style = style;
		this.vertex = vertex;
		this.workflowId = workflowId;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.asT = asT;
		this.type = type;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "node_id", precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "value", length = 40)
	public String getValue() {
		if (this.value == null) {
			return null;
		}
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(this.value);
		return m.replaceAll("");
	}

	public void setValue(String value) {
		if (value == null) {
			this.value = null;
		} else {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(value);
			value = m.replaceAll("");
			this.value = value;
		}
	}

	@Column(name = "style", length = 200)
	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Column(name = "vertex", length = 10)
	public String getVertex() {
		return this.vertex;
	}

	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	@Column(name = "workflow_id", precision = 10, scale = 0)
	public Long getWorkflowId() {
		return this.workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	@Column(name = "x", precision = 22, scale = 0)
	public Long getX() {
		return this.x;
	}

	public void setX(Long x) {
		this.x = x;
	}

	@Column(name = "y", precision = 22, scale = 0)
	public Long getY() {
		return this.y;
	}

	public void setY(Long y) {
		this.y = y;
	}

	@Column(name = "width", precision = 22, scale = 0)
	public Long getWidth() {
		return this.width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	@Column(name = "height", precision = 22, scale = 0)
	public Long getHeight() {
		return this.height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	@Column(name = "as_t", length = 20)
	public String getAsT() {
		return this.asT;
	}

	public void setAsT(String asT) {
		this.asT = asT;
	}

	@Column(name = "type", length = 30)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "description", length = 400)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", value=" + value + ", workflowId="
				+ workflowId + ", type=" + type + ", description="
				+ description + "]";
	}
}