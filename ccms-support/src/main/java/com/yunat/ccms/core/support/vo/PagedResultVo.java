package com.yunat.ccms.core.support.vo;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * 分页显示的数据（前端flexigrid可解析的格式）
 * 
 * @author xiaojing.qu
 * 
 * @param <T>
 */
public class PagedResultVo<T> {
	public Long total;
	/*** 第一页从1开始 */
	public Integer page;
	public List<T> data;

	public PagedResultVo() {
	}

	/**
	 * @param page
	 *            spring data查询的结果
	 */
	public PagedResultVo(Page<T> page) {
		this.total = page.getTotalElements();
		this.page = page.getNumber() + 1;
		this.data = page.getContent();
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
