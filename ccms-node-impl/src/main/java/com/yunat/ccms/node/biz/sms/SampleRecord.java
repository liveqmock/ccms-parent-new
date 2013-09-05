package com.yunat.ccms.node.biz.sms;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "twf_node_sms_sample_record")
public class SampleRecord implements Serializable {
	private static final long serialVersionUID = -2447467726230513904L;
	
	private Long id;
	private Long nodeId;
	private Long subjobId;
	private String uniId;
	private String content;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "node_id")
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "subjob_id")
	public Long getSubjobId() {
		return subjobId;
	}

	public void setSubjobId(Long subjobId) {
		this.subjobId = subjobId;
	}

	@Column(name = "uni_id")
	public String getUniId() {
		return uniId;
	}

	public void setUniId(String uniId) {
		this.uniId = uniId;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}