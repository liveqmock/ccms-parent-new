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
 * 元数据实体：数据视图查询定义
 *
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_query_join_criteria")
public class QueryJoinCriteria implements Serializable {

	private static final long serialVersionUID = -441710172414959174L;

	private Long queryJoinId;
	private Query query;
	private DatabaseColumn leftColumn;
	private DatabaseColumn rightColumn;
	private String joinType;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "query_join_id", unique = true)
	public Long getQueryJoinId() {
		return queryJoinId;
	}

	public void setQueryJoinId(Long queryJoinId) {
		this.queryJoinId = queryJoinId;
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
	@JoinColumn(name = "left_column_id")
	public DatabaseColumn getLeftColumn() {
		return leftColumn;
	}

	public void setLeftColumn(DatabaseColumn leftColumn) {
		this.leftColumn = leftColumn;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "right_column_id")
	public DatabaseColumn getRightColumn() {
		return rightColumn;
	}

	public void setRightColumn(DatabaseColumn rightColumn) {
		this.rightColumn = rightColumn;
	}

	@Column(name = "join_type", length = 32)
	public String getJoinType() {
		return joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
}
