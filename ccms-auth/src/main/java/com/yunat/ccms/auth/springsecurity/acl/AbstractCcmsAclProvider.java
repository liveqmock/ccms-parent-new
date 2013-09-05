package com.yunat.ccms.auth.springsecurity.acl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.afterinvocation.AbstractAclProvider;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;

import com.yunat.ccms.core.support.auth.AclManaged;

/**
 * 本类的子类是用来在某个save或del或之类的方法执行之后,对被操作的model进行相关的acl的增删工作.
 * 接受条件:该方法上有<code>@Secured</code>注解,并且其value与子类中的某个相吻合.
 * 
 * @author wenjian.liang
 */
public abstract class AbstractCcmsAclProvider extends AbstractAclProvider {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public AbstractCcmsAclProvider(final AclService aclService, final String processConfigAttribute) {
		super(aclService, processConfigAttribute,//
				//第三个参数没用,因为我们没有使用父类的hasPermission方法
				Arrays.asList(BasePermission.ADMINISTRATION));
	}

	@Override
	public boolean supports(final ConfigAttribute attribute) {
		return AclUtil.supports(attribute, processConfigAttribute);
	}

	protected Object getDomainObjectInstance(final Object secureObject) {
		Object[] args;
		Class<?>[] paramClasses;
		String methodName;

		if (secureObject instanceof MethodInvocation) {
			final MethodInvocation invocation = (MethodInvocation) secureObject;
			paramClasses = invocation.getMethod().getParameterTypes();
			args = invocation.getArguments();
			methodName = invocation.getMethod().getName();
		} else {
			final JoinPoint jp = (JoinPoint) secureObject;
			final CodeSignature codeSignature = (CodeSignature) jp.getStaticPart().getSignature();
			paramClasses = codeSignature.getParameterTypes();
			args = jp.getArgs();
			methodName = codeSignature.getName();
		}

		//我们至少有以下几种情况:
		//T save(T entity)参数是一个model
		//Iterable<T> save(Iterable<? extends T> entities)参数是Iterable<model>
		//void delete(ID id)参数是个Serializable
		//void delete(T entity)参数是个model
		//void delete(Iterable<? extends T> entities)参数是Iterable<model>
		//void deleteAll()没有参数
		if (paramClasses.length == 0) {
			logger.warn(methodName + "方法没有参数，因此没有对象需要处理。");
			return null;
		}

		for (int i = 0; i < paramClasses.length; i++) {
			final Class<?> paramClass = paramClasses[i];
			if (Iterable.class.isAssignableFrom(paramClass)) {
				final Iterable<?> iterable = (Iterable<?>) args[i];
				final Iterator<?> iterator = iterable.iterator();
				final Collection<Object> models = new LinkedList<Object>();
				while (iterator.hasNext()) {
					final Object element = iterator.next();
					if (isAclManagedClass(element.getClass())) {
						models.add(element);
					}
				}
				return models;
			} else if (isAclManagedClass(paramClass)) {
				return args[i];
			}
		}
		return null;
	}

	protected boolean isAclManagedClass(final Class<?> clazz) {
		final Class<?> processDomainObjectClass = getProcessDomainObjectClass();
		if (processDomainObjectClass.isAssignableFrom(clazz)//
				&& clazz.isAnnotationPresent(AclManaged.class)) {
			return true;
		}
		return false;
	}

	/**
	 * @param model
	 * @return
	 */
	protected ObjectIdentity getOidOfDomainObject(final Object model) {
		return objectIdentityRetrievalStrategy.getObjectIdentity(model);
	}

	/**
	 * @return
	 */
	protected MutableAclService getMutableAclService() {
		return (MutableAclService) aclService;
	}

}