package com.yunat.ccms.rule.center.conf.condition;

import java.util.List;

public class ItemsResponse {
	private Long count;
	private List<ItemWrapper> list;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<ItemWrapper> getList() {
		return list;
	}

	public void setList(List<ItemWrapper> list) {
		this.list = list;
	}

}