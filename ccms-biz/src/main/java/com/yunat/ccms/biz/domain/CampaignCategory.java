package com.yunat.ccms.biz.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tdu_campaign_category")
public class CampaignCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6672651799333858925L;

	private Long categoryId;
	private String categoryValue;
	private Boolean disabled;

	@Id
	@Column(name = "category_id")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "category_value")
	public String getCategoryValue() {
		return categoryValue;
	}

	public void setCategoryValue(String categoryValue) {
		this.categoryValue = categoryValue;
	}

	@Column(name = "disabled", columnDefinition = "BOOLEAN", nullable = false)
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

}
