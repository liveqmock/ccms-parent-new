package com.yunat.ccms.dashboard.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "tb_user_notice")
public class Notice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;
	private String content;
	private Boolean isRead;
	private Date created;
	private Integer noticeType;
	private Integer noticeLevel;
	private Date received;
	private String account;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", precision = 20, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "is_read")
	public Boolean isRead() {
		return isRead;
	}

	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}

	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "notice_type")
	public Integer getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

	@Column(name = "notice_level")
	public Integer getNoticeLevel() {
		return noticeLevel;
	}

	public void setNoticeLevel(Integer noticeLevel) {
		this.noticeLevel = noticeLevel;
	}

	@Column(name = "received")
	public Date getReceived() {
		return received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	@Column(name = "account")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
