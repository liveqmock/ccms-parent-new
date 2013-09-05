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
 * 元数据：数据查询的关联表
 * 
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_query_table")
public class QueryTable implements Serializable {

	private static final long serialVersionUID = 2182501699018045345L;

	private Long queryTableId;
	private Query query;
	private DatabaseTable dbTable;
	private Boolean isMaster;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "query_table_id", unique = true, nullable = false, precision = 20, scale = 0)
	public Long getQueryTableId() {
		return queryTableId;
	}

	public void setQueryTableId(Long queryTableId) {
		this.queryTableId = queryTableId;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "query_id")
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "table_id")
	public DatabaseTable getDbTable() {
		return dbTable;
	}

	public void setDbTable(DatabaseTable dbTable) {
		this.dbTable = dbTable;
	}

	@Column(name = "is_master")
	public Boolean getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(Boolean isMaster) {
		this.isMaster = isMaster;
	}
}
