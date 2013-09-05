package com.yunat.ccms.metadata.pojo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author kevin.jiang 2013-3-15
 */
@Entity
@Table(name = "tm_catalog")
public class Catalog implements Serializable {

	private static final long serialVersionUID = -3584468295033197139L;

	private Long catalogId;
	private int catalogType;
	private Catalog parent;
	private String showName;
	private int showOrder;
	private String platCode;
	private Set<CatalogCriteria> catalogColumns;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "catalog_id", unique = true, nullable = false, precision = 20, scale = 0)
	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	@Column(name = "catalog_type")
	public int getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(int catalogType) {
		this.catalogType = catalogType;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(name = "parent_id")
	public Catalog getParent() {
		return parent;
	}

	public void setParent(Catalog parent) {
		this.parent = parent;
	}

	@Column(name = "show_name", length = 50)
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

	@Column(name = "plat_code", length = 32)
	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "catalog_id")
	public Set<CatalogCriteria> getCatalogColumns() {
		return catalogColumns;
	}

	public void setCatalogColumns(Set<CatalogCriteria> catalogColumns) {
		this.catalogColumns = catalogColumns;
	}

}
