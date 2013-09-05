package com.yunat.ccms.core.support.auth;

import java.util.Collection;
import java.util.EnumSet;

import com.yunat.ccms.core.support.utils.HasIdGetter.HasIntIdGetter;

/**
 * 模块/数据可以支持的操作.
 * 
 * @author wenjian.liang
 */
public enum SupportOp implements HasIntIdGetter {
	// 这些值跟Spring security 的BasePermission基本一致
	/**
	 * 增
	 */
	ADD(2, 'C'), //
	/**
	 * 删
	 */
	DEL(3, 'D'), //
	/**
	 * 改
	 */
	UPDATE(1, 'W'), //
	/**
	 * 查、展示
	 */
	VIEW(0, 'R'), //
	/**
	 * 点击操作。用于链接、按钮、图片等。
	 */
	CLICK(4, 'V'), // v for visit
	;
	public final int id;
	public final int mask;
	public final char code;

	private SupportOp(final int id, final char crud) {
		this.id = id;
		mask = 1 << id;
		code = crud;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public boolean supports(final int supportOps) {
		return (mask & supportOps) != 0;
	}

	public static Collection<SupportOp> supportOps(final int supportOpsMask) {
		final EnumSet<SupportOp> rt = EnumSet.noneOf(SupportOp.class);
		for (final SupportOp o : values()) {
			if (o.supports(supportOpsMask)) {
				rt.add(o);
			}
		}
		return rt;
	}

	public static int toMask(final Collection<SupportOp> supportOps) {
		int rt = 0;
		for (final SupportOp o : supportOps) {
			rt |= o.mask;
		}
		return rt;
	}

	public char getCode() {
		return code;
	}
}