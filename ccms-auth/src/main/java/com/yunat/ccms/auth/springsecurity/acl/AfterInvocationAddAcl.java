package com.yunat.ccms.auth.springsecurity.acl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.auth.PermissionTranslate;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.role.RoleRepository;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.auth.AuthCons;
import com.yunat.ccms.core.support.auth.SupportOp;

/**
 * 在标注了<code>@Secured({AuthCons.ADD_ACL})</code>的方法执行之后,给被操作的model增加acl.
 * 管理员对这些对象有全部权限;
 * 创建者(主人)对这些对象有全部权限;
 * 其他人只有读取权限
 * 
 * @author wenjian.liang
 */
public class AfterInvocationAddAcl extends AbstractCcmsAclProvider {

	@Autowired
	private RoleRepository roleRepository;

	protected RoleSupportOpsMapper roleSupportOpsMapper = new DefaultRoleSupportOpsMapper();

	public AfterInvocationAddAcl(final AclService aclService) {
		super(aclService, AuthCons.ADD_ACL_FOR_SECURED_ANNOTATION);
	}

	/**
	 * 根据传入的第一个AclDomain来删除ACL信息
	 */
	@Override
	public Object decide(final Authentication authentication, final Object securedObject,
			final Collection<ConfigAttribute> config, final Object returnedObject) {
		for (final ConfigAttribute attr : config) {
			if (!supports(attr)) {
				continue;
			}
			logger.debug("创建acl...地点:" + securedObject);
			logger.debug("创建acl...人物:" + authentication.getName());

			final Object model = getDomainObjectInstance(securedObject);
			logger.debug("需要创建acl的对象:" + model);
			if (model instanceof Iterable<?>) {
				final Iterable<?> iterable = (Iterable<?>) model;
				for (final Object o : iterable) {
					createAcl(o);
				}
			} else {
				createAcl(model);
			}
		}

		return returnedObject;
	}

	protected void createAcl(final Object model) {
		final ObjectIdentity oid = getOidOfDomainObject(model);
		if (oid == null) {
			logger.warn("无法生成oid:" + model);
			return;
		}
		new AddAclHelper().addAces(model, LoginInfoHolder.getCurrentUser(), oid);
	}

	protected SupportOp[] supportOpsForCurrentUser() {
		return SupportOp.values();
	}

	/**
	 * 由于同一个对象的方法内部调用时不会触发aop,因此为了让addAces方法的@Transactional起效,在外面包了这个类
	 * 
	 * @author wenjian.liang
	 */
	private class AddAclHelper {
		@Transactional
		public void addAces(final Object model, final User currentUser, final ObjectIdentity oid) {
			final MutableAclService mutableAclService = getMutableAclService();
			final MutableAcl acl = mutableAclService.createAcl(oid);
			//如果用户是管理员,并且私人权限跟管理员身份权限一样,也要增加一个私人身份的ace.因为身份可能会变
			//增加私人权限
			addCurrentUserAce(acl, new PrincipalSid(String.valueOf(currentUser.getId())));
			//增加身份权限
			addAceForRoles(model, acl);

			mutableAclService.updateAcl(acl);
		}
	}

	protected void addAceForRoles(final Object model, final MutableAcl acl) {
		final List<Role> allRoles = roleRepository.findAll();
		int index = AuthCons.ACE_INDEX_OF_CURRENT_USER + 1;
		for (final Role role : allRoles) {
			acl.insertAce(index, mergePermission(roleSupportOpsMapper.getSupportOps(role, model)),//
					new GrantedAuthoritySid(String.valueOf(role.getId())), true);
			index++;
		}
	}

	protected void addCurrentUserAce(final MutableAcl acl, final Sid sid) {
		acl.insertAce(AuthCons.ACE_INDEX_OF_CURRENT_USER, mergePermission(supportOpsForCurrentUser()), sid, true);
	}

	protected Permission mergePermission(final SupportOp[] supportOpsForCurrentUser) {
		final CumulativePermission permission = new CumulativePermission();
		for (final SupportOp supportOp : supportOpsForCurrentUser) {
			final Collection<Permission> permissions = PermissionTranslate.valueOf(supportOp).getPermissions();
			for (final Permission p : permissions) {//其实permissions只有一个元素
				permission.set(p);
			}
		}
		return permission;
	}

	public RoleSupportOpsMapper getRoleSupportOpsMapper() {
		return roleSupportOpsMapper;
	}

	public void setRoleSupportOpsMapper(final RoleSupportOpsMapper roleSupportOpsMapper) {
		this.roleSupportOpsMapper = roleSupportOpsMapper;
	}
}