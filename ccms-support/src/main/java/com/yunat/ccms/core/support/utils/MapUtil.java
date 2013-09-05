package com.yunat.ccms.core.support.utils;

import java.util.Map;

public class MapUtil {

	/**
	 * 一个便利方法,从map中获取值再强转为某类.注意,要在十分清楚map中的值的类型时才使用.
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueFromMap(final Map<?, ?> map, final Object key) {
		return (T) map.get(key);
	}

}
