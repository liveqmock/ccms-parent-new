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
 * 元数据实体：数据查询条件
 * 
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_query_criteria")
public class QueryCriteria implements Serializable {

	private static final long serialVersionUID = -1030548053962351375L;

	private Long queryCriteriaId;
	private Query query;
	private DatabaseColumn databaseColumn;
	private String queryType;
	private String quotaType;
	private String columnExpr;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "query_criteria_id", unique = true, nullable = false)
	public Long getQueryCriteriaId() {
		return queryCriteriaId;
	}

	public void setQueryCriteriaId(Long queryCriteriaId) {
		this.queryCriteriaId = queryCriteriaId;
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
	@JoinColumn(name = "column_id")
	public DatabaseColumn getDatabaseColumn() {
		return databaseColumn;
	}

	public void setDatabaseColumn(DatabaseColumn databaseColumn) {
		this.databaseColumn = databaseColumn;
	}

	@Column(name = "column_expr", length = 100)
	public String getColumnExpr() {
		return columnExpr;
	}

	public void setColumnExpr(String columnExpr) {
		this.columnExpr = columnExpr;
	}

	@Column(name = "query_type", length = 200)
	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	@Column(name = "quota_type", length = 200)
	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}

}
