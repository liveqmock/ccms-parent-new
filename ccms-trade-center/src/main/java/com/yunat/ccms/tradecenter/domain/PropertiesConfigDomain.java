package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 属性配置表
 * @author 李卫林
 *
 */
@Entity
@Table(name = "tb_tc_properties_config")
public class PropertiesConfigDomain {
	/**
	 * 主键
	 */
	private Long pkid;

	/**
	 * 店铺id
	 */
	private String dpId;

	/**
	 * 配置项名称
	 */
	private String name;

	/**
	 * 配置项值
	 */
	private String value;

	/**
	 * 配置项描述
	 */
	private String description;

	/**
	 * 配置项聚合分组
	 */
	private String groupName;

	private Date created;
	private Date updated;

	@Id
	@GeneratedValue
	public Long getPkid() {
		return pkid;
	}
	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	@Column(name="dp_id")
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="group_name")
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
}
