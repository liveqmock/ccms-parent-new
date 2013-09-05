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
 * 元数据实体：索引定义的一组属性
 * 
 * @author kevin.jiang 2013-3-12
 */
@Entity
@Table(name = "tm_catalog_criteria")
public class CatalogCriteria implements Serializable {

	private static final long serialVersionUID = 5573523064525977595L;

	private Long id;
	private Catalog catalog;
	private QueryCriteria queryCriteria;
	private String showName;
	private int showOrder;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "catalog_id")
	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "query_criteria_id")
	public QueryCriteria getQueryCriteria() {
		return queryCriteria;
	}

	public void setQueryCriteria(QueryCriteria queryCriteria) {
		this.queryCriteria = queryCriteria;
	}

	@Column(name = "show_name", length = 100)
	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	@Column(name = "show_order")
	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

}
