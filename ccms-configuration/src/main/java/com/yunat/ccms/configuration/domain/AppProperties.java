package com.yunat.ccms.configuration.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_app_properties")
public class AppProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8045688608236951443L;

	/**
	 * 应用配置-唯一标识
	 */
	private Long propId;
	
	/**
	 * 参数所属组别
	 */
	private String propGroup;
	
	/**
	 * 配置参数名
	 */
	private String propName;
	
	/**
	 * 配置值
	 */
	private String propValue;
	
	/**
	 * 描述
	 */
	private String propDesc;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prop_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPropId() {
		return propId;
	}

	public void setPropId(Long propId) {
		this.propId = propId;
	}

	@Column(name = "prop_group")
	public String getPropGroup() {
		return propGroup;
	}

	public void setPropGroup(String propGroup) {
		this.propGroup = propGroup;
	}

	@Column(name = "prop_name")
	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	@Column(name = "prop_value")
	public String getPropValue() {
		return propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	@Column(name = "prop_desc")
	public String getPropDesc() {
		return propDesc;
	}

	public void setPropDesc(String propDesc) {
		this.propDesc = propDesc;
	}
}