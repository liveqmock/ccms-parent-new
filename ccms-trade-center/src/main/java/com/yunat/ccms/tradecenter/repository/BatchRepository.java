package com.yunat.ccms.tradecenter.repository;

import java.util.List;

public interface BatchRepository{
	/**
	 * 批量插入
	 * @param <T>
	 * @param list
	 */
	<T> void batchInsert(List<T> list);

	/**
	 * 批量更新
	 * @param <T>
	 * @param list
	 */
	<T> void batchUpdate(List<T> list);
}
