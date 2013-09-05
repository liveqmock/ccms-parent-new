package com.yunat.ccms.biz.domain;

import java.io.Serializable;
import java.util.Date;

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

@Entity
@Table(name = "tb_program")
public class Program implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1279390035018627764L;

	private Long progId;
	private String progName;
	private ProgramCategory type;
	private User creator;
	private Date createTime;
	private Date startTime;
	private Date endTime;
	private String progDesc;
	private String comments;
	private String platCode;
	private String edition;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prog_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getProgId() {
		return progId;
	}

	public void setProgId(final Long progId) {
		this.progId = progId;
	}

	@Column(name = "prog_name", unique = true, nullable = false, length = 500)
	public String getProgName() {
		return progName;
	}

	public void setProgName(final String progName) {
		this.progName = progName;
	}

	@ManyToOne
	@JoinColumn(name = "type")
	public ProgramCategory getType() {
		return type;
	}

	public void setType(final ProgramCategory type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name = "creator")
	public User getCreator() {
		return creator;
	}

	public void setCreator(final User creator) {
		this.creator = creator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "prog_desc", length = 1000)
	public String getProgDesc() {
		return progDesc;
	}

	public void setProgDesc(final String progDesc) {
		this.progDesc = progDesc;
	}

	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
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

}
