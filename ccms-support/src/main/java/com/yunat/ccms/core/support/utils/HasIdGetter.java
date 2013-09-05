package com.yunat.ccms.core.support.utils;

public interface HasIdGetter<I extends Comparable<I>> {

	I getId();

	public static interface HasIntIdGetter extends HasIdGetter<Integer> {
		Integer getId();
	}

	public static interface HasLongIdGetter extends HasIdGetter<Long> {
		Long getId();
	}

	public static interface HasStrIdGetter extends HasIdGetter<String> {
		String getId();
	}
}
