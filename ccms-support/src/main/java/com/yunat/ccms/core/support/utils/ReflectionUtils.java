package com.yunat.ccms.core.support.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReflectionUtils {

	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	static {
		final DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
		ConvertUtils.register(dc, Date.class);
	}

	/**
	 * 调用Getter方法.
	 */
	public static Object invokeGetterMethod(final Object obj, final String propertyName) {
		final String getterMethodName = "get" + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用Setter方法.使用value的Class来查找Setter方法.
	 */
	public static void invokeSetterMethod(final Object obj, final String propertyName, final Object value) {
		invokeSetterMethod(obj, propertyName, value, null);
	}

	/**
	 * 调用Setter方法.
	 * 
	 * @param propertyType
	 *            用于查找Setter方法,为空时使用value的Class替代.
	 */
	public static void invokeSetterMethod(final Object obj, final String propertyName, final Object value,
			final Class<?> propertyType) {
		final Class<?> type = propertyType != null ? propertyType : value.getClass();
		final String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		final Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (final IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e);
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		final Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (final IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Assert.notNull(obj, "object不能为空");
		Assert.hasText(fieldName, "fieldName");
		final Class<? extends Object> clazz = obj.getClass();
		final Field field = findField(clazz, fieldName);
		if (field != null) {
			field.setAccessible(true);
		}
		return field;
	}

	/**
	 * 在指定class的继承链上寻找指定名字的字段.如果没找到,返回null,不抛出NoSuchFieldException
	 * 
	 * @param fieldName
	 * @param clazz
	 * @return
	 */
	public static Field findField(final Class<? extends Object> clazz, final String fieldName) {
		for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
			try {
				// TODO:这里改成把c的所有字段取出来遍历对比会不会好一点呢?这样就不会抛异常了.
				return c.getDeclaredField(fieldName);
			} catch (final NoSuchFieldException e) {
				// NOSONAR
				// Field不在当前类定义,继续向上转型
				logger.error("不可能抛出的异常:{}", e);
			}
		}
		return null;
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况.
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		final Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (final Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
	 * args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Assert.notNull(obj, "object不能为空");

		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				final Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

				method.setAccessible(true);

				return method;

			} catch (final NoSuchMethodException e) {
				// NOSONAR
				// Method不在当前类定义,继续向上转型
				logger.error("不可能抛出的异常:{}", e);
			}
		}
		return null;
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
	 * extends HibernateDao<User>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 *         determined
	 */

	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		final Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		final Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数), 组合成List.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 */

	public static List convertElementPropertyToList(final Collection collection, final String propertyName) {
		final List list = new ArrayList();

		try {
			for (final Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (final Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数), 组合成由分割符分隔的字符串.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 * @param separator
	 *            分隔符.
	 */
	public static String convertElementPropertyToString(final Collection collection, final String propertyName,
			final String separator) {
		final List list = convertElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换字符串到相应类型.
	 * 
	 * @param value
	 *            待转换的字符串
	 * @param toType
	 *            转换目标类型
	 */
	public static Object convertStringToObject(final String value, final Class<?> toType) {
		try {
			return ConvertUtils.convert(value, toType);
		} catch (final Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(final Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	/**
	 * 获取类中被申明的方法，程序将会去查找到真是环境中通过这个类型的参数得到第一个符合参数条件的方法.
	 * 此方法不一定是Java运行时调用的方法，因为Java运行时调用什么方法将会根据引用类型决定
	 * ，而不是运行时的对象类型，由于不能获取引用类型，无法定位到这个方法
	 * 所以使用这个方法有一定的风险,但是获取到的方法一定是参数相等，且所有参数都是clazzs里头类型的父类或者父接口
	 * 假如必须使用这个方法，没有重载方法被申明在这个类中，与父类的重载不算 性能会随着参数、类中的方法数量的增加而增加 added by
	 * liujingyu
	 */
	public static Method getDeclearedMethod(final Class<?> targetClass, final String methodName, Class<?>... clazzs)
			throws Exception {
		clazzs = clazzs == null ? new Class<?>[0] : clazzs;
		try {
			final Method method = targetClass.getDeclaredMethod(methodName, clazzs);
			return method;
		} catch (final Exception e) {
			// 找不到去父类里面找，这里不需要打印出来
		}
		for (final Method method : getDeclearedMethods(targetClass, methodName)) {
			if (clazzs.length == method.getParameterTypes().length) {
				boolean flag = false;
				for (int i = 0, n = clazzs.length; i < n; i++) {
					flag = method.getParameterTypes()[i].isAssignableFrom(clazzs[i]);
					if (!flag) {
						break;
					}
				}
				if (flag) {
					return method;
				}
			}
		}
		throw new NoSuchMethodException();
	}

	/**
	 * 获取到重载方法集合 added by liujingyu
	 * 
	 * @param targetClass
	 * @param methodName
	 * @return
	 */
	public static List<Method> getDeclearedMethods(final Class<?> targetClass, final String methodName)
			throws Exception {
		final List<Method> methodList = new LinkedList<Method>();
		for (final Method method : targetClass.getDeclaredMethods()) {
			if (method.getName().equals(methodName)) {
				methodList.add(method);
			}
		}
		return methodList;
	}

	/**
	 * 将会去父类寻找 added by liujingyu
	 * 
	 * @return
	 */
	public static Method getMethod(final Class<?> targetClass, final String methodName, Class<?>... clazzs) {
		if (targetClass == null) {
			return null;
		}
		clazzs = clazzs == null ? new Class<?>[0] : clazzs;
		try {
			final Method method = getDeclearedMethod(targetClass, methodName, clazzs);
			return method;
		} catch (final Exception e) {
			logger.debug(clazzs + "节点，子类没有重写validateNode方法，调用父类", e);
		}
		final Method method = getMethod(targetClass.getSuperclass(), methodName, clazzs);
		return method;
	}

	public static Object invokeNoArgumentMethod(final Object obj, final String methodName) {
		return invokeMethod(obj, methodName, null, null);
	}
}
