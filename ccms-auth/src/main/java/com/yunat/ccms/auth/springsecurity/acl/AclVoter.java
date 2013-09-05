package com.yunat.ccms.auth.springsecurity.acl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.AclEntryVoter;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

/**
 * 本类与父类大同小异.具体差异的地方在该处已注明.
 * 
 * @author wenjian.liang
 */
public class AclVoter extends AclEntryVoter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	//覆盖父类的objectIdentityRetrievalStrategy.父类的是private的并且没有getter!
	protected ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy;
	//覆盖父类的sidRetrievalStrategy.父类的是private的并且没有getter!
	protected SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

	private AclService aclService;

	private final Permission requirePermission;

	public AclVoter(final AclService aclService, final String processConfigAttribute,
			final Permission[] requirePermission) {
		super(aclService, processConfigAttribute, requirePermission);
		//
		final ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy = new CcmsObjectIdentityRetrievalStrategy();
		setObjectIdentityRetrievalStrategy(objectIdentityRetrievalStrategy);
		this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;

		this.requirePermission = AclUtil.merge(Arrays.asList(requirePermission));
		this.aclService = aclService;
		//没有侵入业务代码,所以没有强制要求他们继承什么类或实现什么接口,所以这里是Object.class
		setProcessDomainObjectClass(Object.class);
	}

	@Override
	protected Object getDomainObjectInstance(final Object secureObject) {
		Object[] args;
		Class<?>[] params;

		if (secureObject instanceof MethodInvocation) {
			final MethodInvocation invocation = (MethodInvocation) secureObject;
			params = invocation.getMethod().getParameterTypes();
			args = invocation.getArguments();
		} else {
			final JoinPoint jp = (JoinPoint) secureObject;
			params = ((CodeSignature) jp.getStaticPart().getSignature()).getParameterTypes();
			args = jp.getArgs();
		}

		final Class<?> processDomainObjectClass = getProcessDomainObjectClass();
		for (int i = 0; i < params.length; i++) {
			if (processDomainObjectClass.isAssignableFrom(params[i])//
					&& params[i].isAnnotationPresent(Entity.class)) {//就多了这一个条件:它必须是一个table对应的model
				return args[i];
			}
		}

		return null;
	}

	@Override
	public int vote(final Authentication authentication, final Object object,
			final Collection<ConfigAttribute> attributes) {
		for (final ConfigAttribute attr : attributes) {
			if (!this.supports(attr)) {
				continue;
			}
			// Need to make an access decision on this invocation
			// Attempt to locate the domain object instance to process
			Object domainObject = getDomainObjectInstance(object);

			// If domain object is null, vote to abstain
			if (domainObject == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Voting to abstain - domainObject is null");
				}

				return ACCESS_ABSTAIN;
			}

			// Evaluate if we are required to use an inner domain object
			final String internalMethod = getInternalMethod();
			if (StringUtils.hasText(internalMethod)) {
				try {
					final Class<?> clazz = domainObject.getClass();
					final Method method = clazz.getMethod(internalMethod, new Class[0]);
					domainObject = method.invoke(domainObject, new Object[0]);
				} catch (final NoSuchMethodException nsme) {
					throw new AuthorizationServiceException("Object of class '" + domainObject.getClass()
							+ "' does not provide the requested internalMethod: " + internalMethod);
				} catch (final IllegalAccessException iae) {
					logger.debug("IllegalAccessException", iae);

					throw new AuthorizationServiceException("Problem invoking internalMethod: " + internalMethod
							+ " for object: " + domainObject);
				} catch (final InvocationTargetException ite) {
					logger.debug("InvocationTargetException", ite);

					throw new AuthorizationServiceException("Problem invoking internalMethod: " + internalMethod
							+ " for object: " + domainObject);
				}
			}

			// Obtain the OID applicable to the domain object
			final ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);

			//就为了这一句,就多了这一句...
			if (objectIdentity == null) {
				return ACCESS_ABSTAIN;
			}

			// Obtain the SIDs applicable to the principal
			final List<Sid> sids = sidRetrievalStrategy.getSids(authentication);

			try {
				// Lookup only ACLs for SIDs we're interested in
				final Acl acl = aclService.readAclById(objectIdentity, sids);
				//由于acl每个ace的permission(mask)可能占若干个位
				//所以不可以用isGranted方法
				if (AclUtil.isGranted(AclUtil.mergePermissions(acl, sids), requirePermission)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Voting to grant access");
					}
					return ACCESS_GRANTED;
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Voting to deny access - ACLs returned, but insufficient permissions for this principal");
					}
					return ACCESS_DENIED;
				}
			} catch (final NotFoundException nfe) {
				if (logger.isDebugEnabled()) {
					logger.debug("Voting to deny access - no ACLs apply for this principal");
				}

				return ACCESS_DENIED;
			}
		}

		// No configuration attribute matched, so abstain
		return ACCESS_ABSTAIN;
	}

	public SidRetrievalStrategy getSidRetrievalStrategy() {
		return sidRetrievalStrategy;
	}

	@Override
	public void setSidRetrievalStrategy(final SidRetrievalStrategy sidRetrievalStrategy) {
		this.sidRetrievalStrategy = sidRetrievalStrategy;
	}

	public AclService getAclService() {
		return aclService;
	}

	public void setAclService(final AclService aclService) {
		this.aclService = aclService;
	}
}