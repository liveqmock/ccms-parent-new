package com.yunat.ccms.biz.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tdu_prog_type")
public class ProgramCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8442787484987353313L;

	private Long programCategoryId;
	private String programCategoryValue;
	private Boolean disabled;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prog_type_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getProgramCategoryId() {
		return this.programCategoryId;
	}

	public void setProgramCategoryId(Long id) {
		this.programCategoryId = id;
	}

	@Column(name = "prog_type", length = 15)
	public String getProgramCategoryValue() {
		return this.programCategoryValue;
	}

	public void setProgramCategoryValue(String value) {
		this.programCategoryValue = value;
	}

	@Column(name = "disabled")
	public Boolean getDisabled() {
		return this.disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

}
