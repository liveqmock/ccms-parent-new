package com.yunat.ccms.auth.springsecurity.acl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAclVoter;
import org.springframework.security.core.Authentication;

import com.yunat.ccms.core.support.auth.AuthCons;

/**
 * 专门为权限模块创建/删除acl用的voter,保证其通过.
 * 
 * @author wenjian.liang
 */
public class PermitOperatingAclVoter extends AbstractAclVoter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean supports(final ConfigAttribute attribute) {
		return AclUtil.supports(attribute, AuthCons.ADD_ACL_FOR_SECURED_ANNOTATION, AuthCons.DEL_ACL_FOR_SECURED_ANNOTATION);
	}

	@Override
	public int vote(final Authentication authentication, final Object object,
			final Collection<ConfigAttribute> attributes) {
		return ACCESS_GRANTED;
	}
}
