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
 * 元数据实体：数据查询字段定义
 * 
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_query_column")
public class QueryColumn implements Serializable {

	private static final long serialVersionUID = 6562030537571356968L;

	private Long queryColumnId;
	private DatabaseColumn databaseColumn;
	private Query query;
	private QueryTable queryTable;
	private String columnExpr;
	private String columnName;
	private int columnOrder;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "query_column_id", unique = true, nullable = false)
	public Long getQueryColumnId() {
		return queryColumnId;
	}

	public void setQueryColumnId(Long queryColumnId) {
		this.queryColumnId = queryColumnId;
	}

	@Column(name = "column_expr")
	public String getColumnExpr() {
		return columnExpr;
	}

	public void setColumnExpr(String columnExpr) {
		this.columnExpr = columnExpr;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "column_id")
	public DatabaseColumn getDatabaseColumn() {
		return databaseColumn;
	}

	public void setDatabaseColumn(DatabaseColumn databaseColumn) {
		this.databaseColumn = databaseColumn;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "query_id")
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	@Column(name = "column_name")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Column(name = "column_order")
	public int getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(int columnOrder) {
		this.columnOrder = columnOrder;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "query_table_id")
	public QueryTable getQueryTable() {
		return queryTable;
	}

	public void setQueryTable(QueryTable queryTable) {
		this.queryTable = queryTable;
	}

}
