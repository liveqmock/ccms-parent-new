package com.yunat.ccms.core.support.vo;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * 对前端flexigrid的分页请求参数的封装
 * 
 * @author xiaojing.qu
 * 
 */
public class PageRequestVo implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(PageRequestVo.class);

	/***  */
	private static final long serialVersionUID = 1L;

	/*** 请求页 */
	private Integer page;
	/*** 每页数量 result per page */
	private Integer rp;
	private String sortname;
	private String sortorder;
	private String query;
	private String qtype;

	/**
	 * 暂时只支持对String的处理
	 * 
	 * @return
	 */
	public <T> Specification<T> getSpecification() {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaquery, CriteriaBuilder cb) {
				if (StringUtils.isEmpty(qtype) || StringUtils.isEmpty(query)) {
					return null;
				}
				if (existsAttribute(root, qtype)) {
					// 注意，这个地方不是String的话会报错,需要再多加一种处理
					try {
						return cb.like(root.get(qtype).as(String.class), "%" + query + "%");
					} catch (Exception e) {
						return null;
					}
				}
				return null;
			}
		};
	}

	private <T> boolean existsAttribute(Root<T> root, String attributeName) {
		try {
			root.get(attributeName).as(String.class);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("在类{}中属性:{}不存在,该过滤请求被忽略", root.getModel().getName(), attributeName);
			return false;
		}

	}

	/**
	 * 获取spring data jpa分页参数
	 * 
	 * @return
	 */
	public Pageable getPageable() {
		return new Pageable() {
			@Override
			public int getPageNumber() {
				return page - 1;
			}

			@Override
			public int getPageSize() {
				return rp;
			}

			@Override
			public int getOffset() {
				return (page - 1) * rp;
			}

			@Override
			public Sort getSort() {
				// TODO not implemented yet
				return null;
			}

		};

	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page == null ? 1 : page;
	}

	public Integer getRp() {
		return rp;
	}

	public void setRp(Integer rp) {
		this.rp = rp == null ? 20 : rp;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQtype() {
		return qtype;
	}

	public void setQtype(String qtype) {
		this.qtype = qtype;
	}

}
