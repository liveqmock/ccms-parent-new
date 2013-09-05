package com.yunat.ccms.metadata.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 元数据实体：关联引用字典类型
 * 
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_refer")
public class ReferType implements Serializable {

	private static final long serialVersionUID = 7698311655075516021L;

	private Long referId;
	private String platCode;
	private String referTable;
	private String referKey;
	private String referName;
	private String referCriteriaSql;
	private String orderColumn;
	private String remark;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refer_id", unique = true)
	public Long getReferId() {
		return referId;
	}

	public void setReferId(Long referId) {
		this.referId = referId;
	}

	@Column(name = "plat_code")
	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	@Column(name = "refer_table")
	public String getReferTable() {
		return referTable;
	}

	public void setReferTable(String referTable) {
		this.referTable = referTable;
	}

	@Column(name = "refer_name")
	public String getReferName() {
		return referName;
	}

	public void setReferName(String referName) {
		this.referName = referName;
	}

	@Column(name = "refer_key")
	public String getReferKey() {
		return referKey;
	}

	public void setReferKey(String referKey) {
		this.referKey = referKey;
	}

	@Column(name = "refer_criteria_sql")
	public String getReferCriteriaSql() {
		return referCriteriaSql;
	}

	public void setReferCriteriaSql(String referCriteriaSql) {
		this.referCriteriaSql = referCriteriaSql;
	}

	@Column(name = "order_column")
	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
