package com.yunat.ccms.tradecenter.controller.vo;

import com.yunat.ccms.tradecenter.controller.BaseVO;

public class PageVO extends BaseVO {
	private int total;
	private int page;
	private int totalPage;
    private int pageSize;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
