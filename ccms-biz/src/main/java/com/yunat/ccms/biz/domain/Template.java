package com.yunat.ccms.biz.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.workflow.domain.WorkFlow;

@Entity
@Table(name = "tb_template")
public class Template implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4713389531680919373L;

	private Long templateId;
	private String templateName;
	private User creater;
	private Date createdTime;
	private Date updatedTime;
	private String templateDesc;
	private String comments;
	private WorkFlow workflow;
	private String platCode;
	private String edition;
	private String picUrl;
	private Boolean disabled;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "template_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(final Long templateId) {
		this.templateId = templateId;
	}

	@Column(name = "template_name", unique = true, nullable = false)
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(final String templateName) {
		this.templateName = templateName;
	}

	@ManyToOne
	@JoinColumn(name = "creator")
	public User getCreater() {
		return creater;
	}

	public void setCreater(final User creater) {
		this.creater = creater;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(final Date createdTime) {
		this.createdTime = createdTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(final Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Column(name = "template_desc")
	public String getTemplateDesc() {
		return templateDesc;
	}

	public void setTemplateDesc(final String templateDesc) {
		this.templateDesc = templateDesc;
	}

	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	@Column(name = "disabled", columnDefinition = "BOOLEAN")
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(final Boolean disabled) {
		this.disabled = disabled;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "workflow_id")
	public WorkFlow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(final WorkFlow workflow) {
		this.workflow = workflow;
	}

	@Column(name = "plat_code")
	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(final String platCode) {
		this.platCode = platCode;
	}

	@Column(name = "edition")
	public String getEdition() {
		return edition;
	}

	public void setEdition(final String edition) {
		this.edition = edition;
	}

	@Column(name = "pic_url")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(final String picUrl) {
		this.picUrl = picUrl;
	}

}