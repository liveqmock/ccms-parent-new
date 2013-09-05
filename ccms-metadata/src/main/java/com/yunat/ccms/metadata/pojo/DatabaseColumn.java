package com.yunat.ccms.metadata.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 元数据实体：物理表字段定义
 * 
 * @author kevin.jiang 2013-3-12
 */

@Entity
@Table(name = "tm_db_column")
public class DatabaseColumn implements Serializable {

	private static final long serialVersionUID = 8412749320096155785L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "column_id", unique = true, nullable = false, precision = 20, scale = 0)
	private Long columnId;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "table_id")
	private DatabaseTable table;

	@Column(name = "db_name", length = 30)
	private String columnName;

	@Column(name = "show_name", length = 30)
	private String showName;

	@Column(name = "db_type", length = 30)
	private String columnType;

	@Column(name = "java_type", length = 255)
	private String javaType;

	@Column(name = "business_type", length = 30)
	private String businessType;

	@Column(name = "is_pk", precision = 1, scale = 0)
	private Boolean isPK;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated")
	private Date updated;

	@Column(name = "remark")
	private String remark;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "dic_id")
	private DicType dicType;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "refer_id")
	private ReferType referType;

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public DatabaseTable getTable() {
		return table;
	}

	public void setTable(DatabaseTable table) {
		this.table = table;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public Boolean getIsPK() {
		return isPK;
	}

	public void setIsPK(Boolean isPK) {
		this.isPK = isPK;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public DicType getDicType() {
		return dicType;
	}

	public void setDicType(DicType dicType) {
		this.dicType = dicType;
	}

	public ReferType getReferType() {
		return referType;
	}

	public void setReferType(ReferType referType) {
		this.referType = referType;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
}
