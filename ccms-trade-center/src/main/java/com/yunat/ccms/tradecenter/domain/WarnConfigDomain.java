package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateDeserialize;
import com.yunat.ccms.core.support.json.JsonDateDeserializeParrten;
import com.yunat.ccms.core.support.json.JsonDateSerializer;
import com.yunat.ccms.core.support.json.JsonDateSerializerParrten;

@Entity
@Table(name = "tb_tc_warn_config")
public class WarnConfigDomain {

	/** 主键 **/
	@Id
	@GeneratedValue
	@Column(name = "pkid")
	private Long pkid;

	@Column(name = "dp_id")
	private String dpId;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	@Column(name = "created")
	private Date created;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	@Column(name = "updated")
	private Date updated;

	@Column(name = "warn_type")
	private Integer warnType;

	@JsonSerialize(using = JsonDateSerializerParrten.class)
	@JsonDeserialize(using = JsonDateDeserializeParrten.class)
	@Column(name = "warn_start_time")
	private Date warnStartTime;

	@JsonSerialize(using = JsonDateSerializerParrten.class)
	@JsonDeserialize(using = JsonDateDeserializeParrten.class)
	@Column(name = "warn_end_time")
	private Date warnEndTime;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "op_user")
	private String opUser;

	@Column(name = "is_open")
	private Integer isOpen;

	@Column(name = "is_switch")
	private Integer isSwitch;

	@Column(name = "content")
	private String content;

	@Column(name = "warn_mobiles")
	private String warnMobiles;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Integer getWarnType() {
		return warnType;
	}

	public void setWarnType(Integer warnType) {
		this.warnType = warnType;
	}

	public Date getWarnStartTime() {
		return warnStartTime;
	}

	public void setWarnStartTime(Date warnStartTime) {
		this.warnStartTime = warnStartTime;
	}

	public Date getWarnEndTime() {
		return warnEndTime;
	}

	public void setWarnEndTime(Date warnEndTime) {
		this.warnEndTime = warnEndTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public String getOpUser() {
		return opUser;
	}

	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

	public Integer getIsSwitch() {
		return isSwitch;
	}

	public void setIsSwitch(Integer isSwitch) {
		this.isSwitch = isSwitch;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWarnMobiles() {
		return warnMobiles;
	}

	public void setWarnMobiles(String warnMobiles) {
		this.warnMobiles = warnMobiles;
	}

}
