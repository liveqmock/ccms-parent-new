package com.yunat.ccms.metadata.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 元数据实体：字典值枚举
 * 
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_dic_value")
public class DicTypeValue implements Serializable {

	private static final long serialVersionUID = -4509606764041269055L;

	private Long dicTypeValueId;
	private DicType dicType;
	private DicTypeValue parent;
	private String typeValue;
	private String showName;
	private String remark;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dic_value_id", unique = true, precision = 20, scale = 0)
	public Long getDicTypeValueId() {
		return dicTypeValueId;
	}

	public void setDicTypeValueId(Long dicTypeValueId) {
		this.dicTypeValueId = dicTypeValueId;
	}

	@ManyToOne
	@JoinColumn(name = "dic_id", nullable = false)
	public DicType getDicType() {
		return dicType;
	}

	public void setDicType(DicType dicType) {
		this.dicType = dicType;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(name = "parent_id")
	public DicTypeValue getParent() {
		return parent;
	}

	public void setParent(DicTypeValue parent) {
		this.parent = parent;
	}

	@Column(name = "dic_value")
	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

	@Column(name = "show_name")
	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
