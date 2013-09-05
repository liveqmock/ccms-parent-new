package com.yunat.ccms.module;

import com.yunat.ccms.core.support.utils.HasIdGetter.HasIntIdGetter;

/**
 * 模块加载类型
 * 
 * @author MaGiCalL
 */
public enum ModuleLoadType implements HasIntIdGetter {
	/**
	 * 直接加载
	 */
	NORMAL(1), //
	/**
	 * 第一次获得焦点时加载
	 */
	FIRST_FOCUS(2), //
	;
	public final int id;

	private ModuleLoadType(int id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}
}