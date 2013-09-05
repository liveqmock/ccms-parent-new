package com.yunat.ccms.auth.springsecurity.acl;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.core.support.auth.AuthCons;

/**
 * 在标注了Secured({AuthCons.DEL_ACL})的方法执行之后,删除该方法所改变的记录的相应的acl
 * 
 * @author wenjian.liang
 */
public class AfterInvocationDelAcl extends AbstractCcmsAclProvider {

	public AfterInvocationDelAcl(final AclService aclService) {
		super(aclService, AuthCons.DEL_ACL_FOR_SECURED_ANNOTATION);
	}

	@Override
	public Object decide(final Authentication authentication, final Object securedObject,
			final Collection<ConfigAttribute> attributes, final Object returnedObject) {
		for (final ConfigAttribute attr : attributes) {
			if (!supports(attr)) {
				continue;
			}
			logger.debug("删除acl...地点:" + securedObject);
			logger.debug("删除acl...人物:" + authentication.getName());

			final Object model = getDomainObjectInstance(securedObject);
			logger.debug("需要删除acl的对象:" + model);

			final ObjectIdentity oid = getOidOfDomainObject(model);
			if (oid == null) {
				logger.warn("无法生成oid:" + model);
			} else {
				delAcl(oid);
			}
		}
		return returnedObject;
	}

	/**
	 * @param oid
	 * @param mutableAclService
	 */
	@Transactional
	protected void delAcl(final ObjectIdentity oid) {
		try {
			getMutableAclService().deleteAcl(oid, true);
		} catch (final Exception e) {
			logger.info("删除acl时出现了异常.", e);
		}
	}

}
