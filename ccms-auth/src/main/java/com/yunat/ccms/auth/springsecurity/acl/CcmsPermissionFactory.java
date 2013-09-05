package com.yunat.ccms.auth.springsecurity.acl;

import org.springframework.security.acls.domain.DefaultPermissionFactory;

import com.yunat.ccms.auth.PermissionTranslate;

/**
 * 我们自己加了一个permission叫click,要到PermissionFactory里注册!不然在获取acl的时候就会抛异常!坑啊!
 * 
 * @author wenjian.liang
 */
public class CcmsPermissionFactory extends DefaultPermissionFactory {

	private CcmsPermissionFactory() {
		super();
		registerPermission(PermissionTranslate.CLICK.getPermissions().get(0),//
				PermissionTranslate.CLICK.name());
	}

}
