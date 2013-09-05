package com.yunat.ccms.auth;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

public class AclTest {
	//一个叫张三的人
	final Sid sid = new PrincipalSid("张三");
	//增删改查的授权
	final CumulativePermission permission = new CumulativePermission()//
			.set(BasePermission.CREATE)//
			.set(BasePermission.DELETE)//
			.set(BasePermission.READ)//
			.set(BasePermission.WRITE);
	//一个随意的资源
	final int objectId = 1;
	final ObjectIdentity objectIdentity = new ObjectIdentityImpl(Object.class, objectId);
	//一个acl
	final AclImpl acl = new AclImpl(objectIdentity, objectId, new AclAuthorizationStrategy() {
		@Override
		public void securityCheck(final Acl acl, final int changeType) {
		}
	}, new AuditLogger() {
		@Override
		public void logIfNeeded(final boolean granted, final AccessControlEntry ace) {
		}
	});

	final List<Permission> permissionsToFind = Arrays.asList(BasePermission.CREATE);
	final List<Sid> sidsToFind = Arrays.asList(sid);

	@Before
	public void before() {
		acl.insertAce(0, permission, sid, true);
	}

	public boolean notUsingSpringSecurity(final List<Sid> sidsToFind, final List<Permission> permissionsToFind) {
		final List<AccessControlEntry> aces = acl.getEntries();
		for (final AccessControlEntry ace : aces) {
			for (final Sid sid : sidsToFind) {
				if (ace.getSid().equals(sid)) {
					final Permission p = ace.getPermission();
					final int mask = p.getMask();
					for (final Permission p2 : permissionsToFind) {
						if ((mask & p2.getMask()) != 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean usingSpringSecurity(final List<Sid> sids, final List<Permission> permissions) {
		return acl.isGranted(Arrays.asList(BasePermission.CREATE), Arrays.asList(sid), false);
	}

	@Test
	public void test1() {
		System.out.println("@@@@@@AclServiceImpl.main():"
		//会抛出异常Unable to locate a matching ACE for passed permissions and SIDs
				+ acl.isGranted(permissionsToFind, sidsToFind, false));
	}

	@Test
	public void test2() {
		System.out.println("@@@@@@AclTest.test2():" + notUsingSpringSecurity(sidsToFind, permissionsToFind));
	}
}
