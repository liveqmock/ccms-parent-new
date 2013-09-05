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
 * 元数据：用来表达一个可保存的自定义查询
 * 
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_query")
public class Query implements Serializable {

	private static final long serialVersionUID = 1379790414085274670L;

	private Long queryId;
	private String code;
	private String showName;
	private String phyView;
	private String platCode;

	private Set<QueryColumn> columns = new HashSet<QueryColumn>();
	private Set<QueryTable> tables = new HashSet<QueryTable>();
	private Set<QueryCriteria> criterias = new HashSet<QueryCriteria>();
	private Set<QueryJoinCriteria> joins = new HashSet<QueryJoinCriteria>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "query_id", unique = true, nullable = false)
	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public String getCode() {
		return code;
	}

	@Column(name = "code", length = 50)
	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "show_name", length = 50)
	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	@Column(name = "phy_view", length = 32)
	public String getPhyView() {
		return phyView;
	}

	public void setPhyView(String phyView) {
		this.phyView = phyView;
	}

	@Column(name = "plat_code", length = 32)
	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "query")
	public Set<QueryColumn> getColumns() {
		return columns;
	}

	public void setColumns(Set<QueryColumn> columns) {
		this.columns = columns;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "query")
	public Set<QueryTable> getTables() {
		return tables;
	}

	public void setTables(Set<QueryTable> tables) {
		this.tables = tables;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "query")
	public Set<QueryCriteria> getCriterias() {
		return criterias;
	}

	public void setCriterias(Set<QueryCriteria> criterias) {
		this.criterias = criterias;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "query")
	public Set<QueryJoinCriteria> getJoins() {
		return joins;
	}

	public void setJoins(Set<QueryJoinCriteria> joins) {
		this.joins = joins;
	}

}
