package com.yunat.ccms.metadata.pojo;

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
/**
 * 元数据实体实体：物理表定义
 * @author kevin.jiang
 * 2013-3-12
 */
@Entity
@Table(name = "tm_db_table")
public class DatabaseTable implements Serializable {

	private static final long serialVersionUID = 9096808311397664391L;

	private Long tableId;
	private String dbName;		//数据库名称
	private String showName;	//表名称
	private String pkColumn;	//主键字段
	private String platCode;	//平台代码
	private Date created;		//创建时间
	private Date updated;		//更新时间
	private String remark;		//备注

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "table_id", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	@Column(name = "db_name", nullable = false, length = 50)
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Column(name = "show_name", nullable = false, length = 100)
	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	@Column(name = "pk_column", length = 50)
	public String getPkColumn() {
		return pkColumn;
	}

	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}

	@Column(name = "plat_code", length = 20)
	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated")
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}