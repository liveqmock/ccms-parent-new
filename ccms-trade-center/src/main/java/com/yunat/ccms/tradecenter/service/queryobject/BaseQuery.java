package com.yunat.ccms.tradecenter.service.queryobject;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 */
public class BaseQuery implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6573080478088203847L;

	private static final Integer defaultPageSize = 20;

	private Integer totalItem;
	private Integer pageSize;
	private Integer currentPage;

	// for paging
	private int startRow;
	private int endRow;

	private String firstOrder;

	private String firstOrderSort = "asc";

	private String  secondOrder;

	private String secondOrderSort = "asc";

	protected Integer getDefaultPageSize() {
		return defaultPageSize;
	}

	public boolean isFirstPage() {
		return this.getCurrentPage().intValue() == 1;
	}

	public int getPreviousPage() {
		int back = this.getCurrentPage().intValue() - 1;

		if (back <= 0) {
			back = 1;
		}

		return back;
	}

	public boolean isLastPage() {
		return this.getTotalPage() == this.getCurrentPage().intValue();
	}

	public int getNextPage() {
		int back = this.getCurrentPage().intValue() + 1;

		if (back > this.getTotalPage()) {
			back = this.getTotalPage();
		}

		return back;
	}

	/**
	 * @return Returns the currentPage.
	 */
	public Integer getCurrentPage() {
		if (currentPage == null || currentPage.intValue() == 0) {
			return 1;
		}

		return currentPage;
	}

	/**
	 * @param currentPage
	 *            The currentPage to set.
	 */
	public void setCurrentPage(Integer page) {
		if ((page == null) || (page.intValue() <= 0)) {
			this.currentPage = null;
		} else {
			this.currentPage = page;
		}
		setStartEndRow();
	}

	private void setStartEndRow() {
		this.startRow = this.getPageSize().intValue() * (this.getCurrentPage().intValue() - 1);
		this.endRow = this.startRow + this.getPageSize().intValue();
	}

	/**
	 * @return Returns the pageSize.
	 */
	public Integer getPageSize() {
		if (pageSize == null) {
			return getDefaultPageSize();
		}

		return pageSize;
	}

	/**
	 * @param pageSize
	 *            The pageSize to set.
	 */
	public void setPageSize(Integer pageSize) {
		if ((pageSize == null) || (pageSize.intValue() <= 0)) {
			this.pageSize = null;
		} else {
			this.pageSize = pageSize;
		}
		setStartEndRow();
	}

	/**
	 * @return Returns the totalItem.
	 */
	public Integer getTotalItem() {
		if (totalItem == null) {
			return 0;
		}

		return totalItem;
	}

	/**
	 * @param totalItem The totalItem to set.
	 */
	public void setTotalItem(Integer totalItem) {
		this.totalItem = totalItem;

		int current = this.getCurrentPage().intValue();
		int lastPage = this.getTotalPage();

		if (current > lastPage) {
			this.setCurrentPage(lastPage);
		}
	}

	public int getTotalPage() {
		int pageSize = this.getPageSize().intValue();
		int total = this.getTotalItem().intValue();
		int result = total / pageSize;

		if ((total == 0) || ((total % pageSize) != 0)) {
			result++;
		}

		return result;
	}

	public int getPageLastItem() {
		int cPage = this.getCurrentPage().intValue();
		int pgSize = this.getPageSize().intValue();
		int assumeLast = pgSize * cPage;
		int totalItem = getTotalItem().intValue();

		if (assumeLast > totalItem) {
			return totalItem;
		} else {
			return assumeLast;
		}
	}

	/**
	 * @return Returns the endRow.
	 */
	public int getEndRow() {
		return endRow;
	}

	/**
	 * @param endRow
	 *            The endRow to set.
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	/**
	 * @return Returns the startRow.
	 */
	public int getStartRow() {
		return startRow;
	}

	/**
	 * @param startRow
	 *            The startRow to set.
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getFirstOrder() {
		return firstOrder;
	}

	public void setFirstOrder(String firstOrder) {
		this.firstOrder = firstOrder;
	}

	public String getSecondOrder() {
		return secondOrder;
	}

	public void setSecondOrder(String secondOrder) {
		this.secondOrder = secondOrder;
	}

	public String getFirstOrderSort() {
		return firstOrderSort;
	}

	public void setFirstOrderSort(String firstOrderSort) {
		this.firstOrderSort = firstOrderSort;
	}

	public String getSecondOrderSort() {
		return secondOrderSort;
	}

	public void setSecondOrderSort(String secondOrderSort) {
		this.secondOrderSort = secondOrderSort;
	}
}
