package com.yunat.ccms.core.support.cons;

import com.yunat.ccms.core.support.utils.HasIdGetter.HasIntIdGetter;

/**
 * CCMS的产品版本.
 * 
 * @author wenjian.liang
 * 
 */
public enum ProductEdition implements HasIntIdGetter {
	/**
	 * 免费版
	 */
	FREE(0), //
	/**
	 * 基础版L1
	 */
	BASIC_L1(1), //
	/**
	 * 基础版L2
	 */
	BASIC_L2(2), //
	/**
	 * 基础版L3
	 */
	BASIC_L3(3), //
	/**
	 * 标准版
	 */
	STANDARD(4), //
	;
	public final int id;

	private ProductEdition(final int id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}
	
}
