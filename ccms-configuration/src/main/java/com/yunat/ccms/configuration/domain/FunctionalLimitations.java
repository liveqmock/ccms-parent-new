package com.yunat.ccms.configuration.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_function_limit")
public class FunctionalLimitations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6209398919431886858L;

	/**
	 * 数据项-唯一标示
	 */
	private Long id;
	
	/**
	 * 系统规格版本
	 */
	private String edition;
	
	/**
	 * 限制项-名称
	 */
	private String limitItem;
	
	/**
	 * 限制项-对应限制数量
	 */
	private Integer limitCount;
	
	/**
	 * 描述-说明
	 */
	private String desc;
	
	/**
	 * 是否有效
	 */
	private Boolean disabled;
	
	/**
	 * 创建时间
	 */
	private Date createAt;
	
	/**
	 * 最新更新时间
	 */
	private Date updateAt;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "edition")
	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	@Column(name = "limit_item")
	public String getLimitItem() {
		return limitItem;
	}

	public void setLimitItem(String limitItem) {
		this.limitItem = limitItem;
	}

	@Column(name = "limit_count")
	public Integer getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}

	@Column(name = "description")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "disabled", columnDefinition = "BOOLEAN")
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@Column(name = "create_at")
	@Temporal(value = TemporalType.DATE)
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@Column(name = "update_at")
	@Temporal(value = TemporalType.DATE)
	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	
}