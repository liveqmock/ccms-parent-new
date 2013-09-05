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
import com.yunat.ccms.core.support.auth.AclManaged;
import com.yunat.ccms.workflow.domain.WorkFlow;

@AclManaged("campaign")
@Entity
@Table(name = "tb_campaign")
public class Campaign implements Serializable {

	private static final long serialVersionUID = 4968409736832912888L;

	private Long campId;
	private Program prog;
	private String campName;
	private CampaignCategory campCategory;
	private CampaignStatus campStatus;
	private User creater;
	private User investigator;
	private Date createdTime;
	private Date startTime;
	private Date endTime;
	private String campDesc;
	private String comments;
	private String workflowType;
	private WorkFlow workflow;
	private String platCode;
	private String edition;
	private String picUrl;
	private Boolean disabled;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "camp_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCampId() {
		return campId;
	}

	public void setCampId(final Long campId) {
		this.campId = campId;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "prog_id")
	public Program getProg() {
		return prog;
	}

	public void setProg(final Program prog) {
		this.prog = prog;
	}

	@Column(name = "camp_name", length = 500)
	public String getCampName() {
		return campName;
	}

	public void setCampName(final String campName) {
		this.campName = campName;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "camp_type")
	public CampaignCategory getCampCategory() {
		return campCategory;
	}

	public void setCampCategory(final CampaignCategory campCategory) {
		this.campCategory = campCategory;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "camp_status")
	public CampaignStatus getCampStatus() {
		return campStatus;
	}

	public void setCampStatus(final CampaignStatus campStatus) {
		this.campStatus = campStatus;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "creater")
	public User getCreater() {
		return creater;
	}

	public void setCreater(final User creater) {
		this.creater = creater;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "investigator")
	public User getInvestigator() {
		return investigator;
	}

	public void setInvestigator(final User investigator) {
		this.investigator = investigator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time", length = 7)
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(final Date createdTime) {
		this.createdTime = createdTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", length = 7)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", length = 7)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "camp_desc", length = 1000)
	public String getCampDesc() {
		return campDesc;
	}

	public void setCampDesc(final String campDesc) {
		this.campDesc = campDesc;
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

	@Column(name = "workflow_type")
	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(final String workflowType) {
		this.workflowType = workflowType;
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