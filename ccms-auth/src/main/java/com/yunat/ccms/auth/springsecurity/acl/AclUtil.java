package com.yunat.ccms.auth.springsecurity.acl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import com.google.common.collect.Sets;

public class AclUtil {

	private static final Logger logger = LoggerFactory.getLogger(AclUtil.class);

	public static boolean isGranted(final Permission permission, final Permission target) {
		return (permission.getMask() & target.getMask()) != 0;
	}

	/**
	 * <pre>
	 * 这个方法将acl中符合sid的ace的permission合并成一个.
	 * 我们不使用acl.isGrant()方法来检验它是否包含sids和permissions的组合.
	 * 这个方法粒度到permission为止,它不会检查mask的包含性.
	 * 我们检验acl的ace的permission的mask是否包含我们需要的mask.
	 * 比如:
	 * acl中一个ace的mask=7,即表示sid对oid有1,2,4三个权限,
	 * 但如果调用isGrant()时传入的是一个表示权限1和2的Permission,则返回为false
	 * 
	 * 不知道是不是springSecurity的一个小坑
	 * 可参考{@link com.yunat.ccms.auth.AclTest}
	 * </pre>
	 */
	public static Permission mergePermissions(final Acl acl, final List<Sid> sidsToFind) {
		final Collection<AccessControlEntry> grantedAces = grantedPermissions(acl, sidsToFind);
		return mergeAces(grantedAces);
	}

	public static Permission mergeAces(final Collection<AccessControlEntry> grantedAces) {
		final CumulativePermission permission = new CumulativePermission();
		for (final AccessControlEntry p : grantedAces) {
			permission.set(p.getPermission());
		}
		return permission;
	}

	public static Permission merge(final Collection<Permission> permissions) {
		final CumulativePermission permission = new CumulativePermission();
		for (final Permission p : permissions) {
			permission.set(p);
		}
		return permission;
	}

	/**
	 * @param acl
	 * @param sidsToFind
	 * @return
	 */
	public static Collection<AccessControlEntry> grantedPermissions(final Acl acl, final List<Sid> sidsToFind) {
		final List<AccessControlEntry> aces = acl.getEntries();
		final Collection<AccessControlEntry> grantedAces = Sets.newHashSet();//只有granting的ace才加入
		for (final AccessControlEntry ace : aces) {
			for (final Sid sid : sidsToFind) {
				if (ace.getSid().equals(sid)) {
					if (ace.isGranting()) {
						grantedAces.add(ace);
					} else {
						//此ace是表示"拒绝进入",则要检查已有的permission有没有冲突的.
						removeNotGranting(grantedAces, ace, sid);
					}
				}
			}//for sid
		}//for ace
		return grantedAces;
	}

	/**
	 * @param grantedAces
	 * @param ace
	 * @param sid
	 * @param permissionOfAce
	 */
	static void removeNotGranting(final Collection<AccessControlEntry> grantedAces, final AccessControlEntry ace,
			final Sid sid) {
		final Permission permissionOfAce = ace.getPermission();
		final Iterator<AccessControlEntry> it = grantedAces.iterator();
		while (it.hasNext()) {
			final AccessControlEntry aceAlreadyHave = it.next();
			if (!isGranted(aceAlreadyHave.getPermission(), permissionOfAce)) {
				if (sid instanceof PrincipalSid) {//私人身份权重高
					if (aceAlreadyHave.getSid() instanceof PrincipalSid) {
						logger.warn("授权矛盾:" + aceAlreadyHave.getId()//
								+ "为" + aceAlreadyHave.getPermission().getMask() + ","//
								+ ace.getId() + "为" + ace.getPermission().getMask());
						it.remove();
					} else {
					}
				} else {//角色身份权重较低
					if (aceAlreadyHave.getSid() instanceof PrincipalSid) {
					} else {
						logger.warn("授权矛盾:" + aceAlreadyHave.getId()//
								+ "为" + aceAlreadyHave.getPermission().getMask() + ","//
								+ ace.getId() + "为" + ace.getPermission().getMask());
					}
				}
			}
		}
	}

	static boolean supports(final ConfigAttribute attribute, final String... processConfigAttributes) {
		if (attribute == null) {
			return false;
		}
		String attr = attribute.getAttribute();
		if (attr == null) {
			return false;
		}
		attr = attr.trim().replace("_", "");
		for (final String s : processConfigAttributes) {
			if (attr.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
}
