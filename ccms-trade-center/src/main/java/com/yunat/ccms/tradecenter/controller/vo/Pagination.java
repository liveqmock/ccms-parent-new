package com.yunat.ccms.tradecenter.controller.vo;

import java.util.List;

import com.yunat.ccms.tradecenter.controller.BaseVO;

/**
 * 页面分页展示对象
 *
 * @author ming.peng
 * @date 2013-6-9
 * @since 4.2.0
 */
public class Pagination<T> extends BaseVO {

	private int pageSize;// 每页记录数
	private int currPage;// 当前页
	private int totalPages;// 总页数
	private long totalElements;// 总记录数
	private List<T> content; // 内容

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
