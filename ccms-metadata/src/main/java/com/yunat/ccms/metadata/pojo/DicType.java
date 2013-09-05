package com.yunat.ccms.metadata.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 元数据实体：字典类型
 *
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_dic")
public class DicType implements Serializable {

	private static final long serialVersionUID = -6700783911608721342L;

	private Long dicTypeId;
	private String showName;
	private String platCode;
	private Set<DicTypeValue> values = new HashSet<DicTypeValue>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dic_id", unique = true)
	public Long getDicTypeId() {
		return dicTypeId;
	}

	public void setDicTypeId(Long dicTypeId) {
		this.dicTypeId = dicTypeId;
	}

	@Column(name = "show_name", length = 32)
	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	@Column(name = "plat_code", length = 50)
	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "dicType")
	public Set<DicTypeValue> getValues() {
		return values;
	}

	public void setValues(Set<DicTypeValue> values) {
		this.values = values;
	}

}
