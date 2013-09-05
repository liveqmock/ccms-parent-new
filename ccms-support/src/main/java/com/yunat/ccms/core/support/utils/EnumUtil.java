package com.yunat.ccms.core.support.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Maps;

public class EnumUtil<E extends Enum<E>> {

	private EnumUtil() {
	}

	/**
	 * 根据名字获取一个枚举值,忽略大小写
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static <E extends Enum<E>> E valueOfIgnoreCase(final Class<E> clazz, final String name) {
		if (clazz == null) {
			return null;
		}
		final E[] es = clazz.getEnumConstants();
		for (final E e : es) {
			if (e.name().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}

	private static class EnumInfo<E extends Enum<E>> {
		Map<Object, E> keyEnumMap = Maps.newHashMap();
	}

	private static final ConcurrentHashMap<Class<?>, ConcurrentHashMap<KeyAccessor<?, ?>, EnumInfo<?>>> map = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<KeyAccessor<?, ?>, EnumInfo<?>>>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <E extends Enum<E>> ConcurrentHashMap<KeyAccessor<?, E>, EnumInfo<E>> mapForEnum(final Class<?> c) {
		return (ConcurrentHashMap) map.get(c);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E extends Enum<E>> Map<KeyAccessor<?, E>, EnumInfo<E>> reg(final Class<E> c,
			final KeyAccessor<?, E> idFieldSelector) {
		ConcurrentHashMap<KeyAccessor<?, E>, EnumInfo<E>> m = mapForEnum(c);
		if (m == null) {
			m = new ConcurrentHashMap<KeyAccessor<?, E>, EnumInfo<E>>();
			map.putIfAbsent(c, (ConcurrentHashMap) m);
		}
		EnumInfo<E> i = m.get(idFieldSelector);
		if (i == null) {
			i = new EnumInfo<E>();
			final E[] values = c.getEnumConstants();
			for (final E e : values) {
				i.keyEnumMap.put(idFieldSelector.extract(e), e);
			}
			m.putIfAbsent(idFieldSelector, i);
		}
		return m;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E extends Enum<E> & HasIdGetter<?>> E get(final Class<E> c, final Object key) {
		return (E) get(c, (KeyAccessor) IdAccessor.INSTANCE, key);
	}

	public static <E extends Enum<E>> E get(final Class<E> c, final KeyAccessor<?, E> keyAccessor, final Object key) {
		Map<KeyAccessor<?, E>, EnumInfo<E>> m = mapForEnum(c);
		if (m == null) {
			m = reg(c, keyAccessor);
		}
		final EnumInfo<E> i = m.get(keyAccessor);
		if (i == null) {
			return null;
		}
		return i.keyEnumMap.get(key);
	}
}
