package com.yunat.ccms.auth.springsecurity.acl;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Lists;
import com.yunat.ccms.core.support.auth.AclManaged;

/**
 * <pre>
 * 参考{@link AclManaged}
 * 
 * </pre>
 * 
 * @author wenjian.liang
 */
public class CcmsObjectIdentityRetrievalStrategy extends ObjectIdentityRetrievalStrategyImpl {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ObjectIdentity getObjectIdentity(final Object domainObject) {
		final AclManaged aclManaged = AnnotationUtils.findAnnotation(domainObject.getClass(), AclManaged.class);
		// acl的所谓的class_id建议用AclClassId注解指定!
		final String typeName;
		if (aclManaged == null) {
			typeName = domainObject.getClass().getName();
		} else {
			if (aclManaged.value().isEmpty()) {
				logger.warn(domainObject.getClass() + "的AclClassId值为空");
				typeName = domainObject.getClass().getName();
			} else {
				typeName = aclManaged.value();
			}
		}

		final Serializable idValue = getIdValue(domainObject);
		if (idValue == null) {
			return null;
		}

		return new ObjectIdentityImpl(typeName, idValue);
	}

	protected Serializable getIdValue(final Object domainObject) {
		final Class<?> clazz = domainObject.getClass();
		// 直接用getId
		try {
			final AclManaged aclManaged = AnnotationUtils.findAnnotation(clazz, AclManaged.class);
			final Method getIdMethod = clazz.getMethod(aclManaged.getIdMethodName());
			if (getIdMethod != null) {
				final Object id = ReflectionUtils.invokeMethod(getIdMethod, domainObject);
				if (id == null) {
					logger.warn("此对象id为空:" + domainObject);
					return null;
				}
				if (!(id instanceof Serializable)) {
					logger.warn("此对象id不能序列化:" + domainObject + " ,id:" + id);// 来到这里就扯淡了吧?
					return null;
				}
				return (Serializable) id;
			}
		} catch (final NoSuchMethodException e) {
			// 没有getId方法就不用它呗
		} catch (final SecurityException e) {
			logger.debug(domainObject.getClass().getName() + "getId方法访问不能访问。", e);
		}
		// 没有getId方法

		// 用标注了@Id注解的字段
		final Field[] fields = clazz.getDeclaredFields();

		Serializable idValueStr = concatIdValues(domainObject, fields);
		if (idValueStr == null) {
			idValueStr = concatIdValues(domainObject, ReflectionUtils.getAllDeclaredMethods(clazz));
		}
		if (idValueStr == null) {
			logger.warn(domainObject.getClass().getName() + "没有id字段。");
			return null;
		}
		if (String.valueOf(idValueStr).isEmpty()) {
			logger.warn("此对象的id字段值都是空字符串:" + domainObject);
			return null;
		}
		return idValueStr;
	}

	protected Serializable concatIdValues(final Object domainObject, final AnnotatedElement[] annotatedElements) {
		final List<AnnotatedElement> idFields = Lists.newArrayListWithExpectedSize(annotatedElements.length);

		for (final AnnotatedElement e : annotatedElements) {
			if (e.isAnnotationPresent(Id.class)) {
				idFields.add(e);
			}
		}
		if (idFields.isEmpty()) {
			return null;
		}

		if (idFields.size() == 1) {
			final AnnotatedElement annotatedElement = idFields.get(0);
			final Serializable value = fieldValue(domainObject, annotatedElement);
			if (value == null) {
				logger.warn("此对象的id(字段名为" + ((Member) annotatedElement).getName() + ")为空:" + domainObject);
				return null;
			}
			return value;
		}

		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final AnnotatedElement e : idFields) {
			final Serializable value = fieldValue(domainObject, e);
			sb.append(value);
			if (first) {
				first = false;
			} else {
				sb.append(" - ");
			}
		}
		if (sb.length() == 0) {
			logger.warn("此对象的id字段值都是空字符串:" + domainObject);
			return null;
		}
		return sb.toString();
	}

	protected Serializable fieldValue(final Object obj, final AnnotatedElement annotatedElement) {
		if (annotatedElement instanceof Field) {// a field
			final Field f = (Field) annotatedElement;
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
			try {
				return (Serializable) f.get(obj);
			} catch (final IllegalArgumentException e) {
				logger.warn("获取" + obj.getClass().getName() + "的id字段" + f.getName() + "的值失败。", e);
			} catch (final IllegalAccessException e) {
				logger.warn("获取" + obj.getClass().getName() + "的id字段" + f.getName() + "的值失败。", e);
			} catch (final ClassCastException e) {
				logger.warn(obj.getClass().getName() + "的id字段" + f.getName() + "不可序列化。", e);
			}
		} else {// a method
			final Method m = (Method) annotatedElement;
			if (!m.isAccessible()) {
				m.setAccessible(true);
			}
			try {
				return (Serializable) ReflectionUtils.invokeMethod(m, obj);
			} catch (final ClassCastException e) {
				logger.warn(obj.getClass().getName() + "的id字段" + m.getName() + "不可序列化。", e);
			}
		}
		return null;
	}
}
