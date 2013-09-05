package com.yunat.ccms.configuration.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_app_expired")
public class AppExpired implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5147484359609533076L;

	/**
	 * 调用渠道系统的username
	 */
	private String associateUsername;
	
	/**
	 * TAOBAO应用订购过期时间
	 */
	private Date expiredDate;

	@Id
	@Column(name = "associate_username", nullable = false, unique = true)
	public String getAssociateUsername() {
		return associateUsername;
	}

	public void setAssociateUsername(String associateUsername) {
		this.associateUsername = associateUsername;
	}

	@Column(name = "expired_date")
	@Temporal(value = TemporalType.DATE)
	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	
}