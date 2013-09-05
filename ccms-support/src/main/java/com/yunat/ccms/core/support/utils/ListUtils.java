package com.yunat.ccms.core.support.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings("rawtypes")
public final class ListUtils {

	public static <E> List<E> toList(final Iterable<E> iterable) {
		if (iterable instanceof List<?>) {
			return (List<E>) iterable;
		}
		final List<E> list = new ArrayList<E>();
		for (final E e : iterable) {
			list.add(e);
		}
		return list;
	}

	/**
	 * 把一个子类对象的集合转化成父类对象的集合.用于搞定关于泛型强转之类问题那些恶心的编译器警告
	 * 
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, T1 extends T> Collection<T> suit(final Collection<T1> source) {
		return (Collection<T>) source;
	}

	/**
	 * 判断集合是否为空 包括null
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(final Collection<?> collection) {
		return null == collection || collection.isEmpty();
	}

	/**
	 * <p>
	 * 提取集合中的对象的某个属性,重新组合成列表.
	 * </p>
	 * 
	 * 如果来源集合为<code>null</code>返回空列表。 <br>
	 * 如果来源集合包含<code>null</code>元素，如： <code>[obj1,obj2,null,obj3]</code>，则在对应
	 * <code>null</code>的位置返回<code>null</code>值，其余位置返回正常值，即：
	 * <code>[prop1,prop2,null,prop3]</code>
	 * 
	 * @param collection
	 *            来源集合.可以为<code>null</code>
	 * @param propertityName
	 *            要提取的属性名.
	 * 
	 * @throws IllegalArgumentException
	 *             如果要提取的属性名为<code>null</code>或该属性无法访问
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> List<T> fetchPropertyToList(final Collection<S> collection, final String propertyName) {

		if (null == propertyName) {
			throw new IllegalArgumentException("字段名不能为空");
		}

		if (null == collection) {
			return Collections.emptyList();
		}

		return (List<T>) CollectionUtils.collect(collection, new Transformer() {

			@Override
			public Object transform(final Object input) {
				if (null == input) {
					return null;
				}
				try {
					return PropertyUtils.getProperty(input, propertyName);
				} catch (final Exception e) {
					throw new IllegalArgumentException(input.getClass() + "的" + propertyName + "属性无法访问", e);
				}
			}
		});

	}

	/**
	 * 提取集合中的对象的属性,组合成由分割符（如,;等）分隔的字符串.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertityName
	 *            要提取的属性名.
	 * @param separator
	 *            分隔符.
	 */
	public static String fetchPropertyToString(final Collection<?> collection, final String propertyName,
			final String separator) {
		final List<String> list = fetchPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	public final static Long getLong(final String[] str, final int i) {
		if (str == null || str.length < i + 1) {
			return null;
		}
		return getLong(Arrays.asList(str), i);
	}

	public final static String getString(final String[] str, final int i) {
		if (str == null || str.length < i + 1) {
			return null;
		}
		return str[i];
	}

	public final static String getString(final List list, final int i) {
		if (list == null || list.size() < i + 1) {
			return null;
		}
		return list.get(i).toString();
	}

	public final static Date getDate(final List<Date> list, final int i) {
		if (list == null || list.size() < i + 1) {
			return null;
		}
		return list.get(i);
	}

	public final static Integer getInteger(final List list, final int i) {
		if (list == null || list.size() < i + 1) {
			return null;
		}
		return HStringUtils.safeToInteger(list.get(i) != null ? list.get(i).toString() : "");
	}

	public final static Long getLong(final List list, final int i) {
		if (list == null || list.size() < i + 1) {
			return null;
		}
		return HStringUtils.safeToLong(list.get(i) != null ? list.get(i).toString() : "");
	}

	public final static Boolean getBoolean(final List list, final int i) {
		if (list == null || list.size() < i + 1) {
			return null;
		}
		return new Boolean("1".equals(list.get(i).toString()) || "true".equalsIgnoreCase(list.get(i).toString()) ? true
				: false);
	}

	/**
	 * 在一个集合中寻找指定的元素(使用equals方法比较是否相同). 如果找到,返回集合中的该元素,注意此元素可能不==指定的元素
	 * 如果没找到,返回null
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static <E> E find(final Collection<E> source, final E target) {
		for (final E e : source) {
			if (e.equals(target)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 在一个集合中寻找指定元素(使用equals方法比较是否相同),返回其下标.如果没找到,返回-1
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static int findIndex(final Collection<?> source, final Object target) {
		int index = 0;
		for (final Object o : source) {
			if (o.equals(target)) {
				return index;
			}
			++index;
		}
		return -1;
	}
}
