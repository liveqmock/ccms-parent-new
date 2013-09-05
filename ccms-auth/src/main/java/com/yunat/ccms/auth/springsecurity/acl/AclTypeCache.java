package com.yunat.ccms.auth.springsecurity.acl;

/**
 * 缓存所有的AclType(acl_class表)以便快速查阅该类型是否有acl管理.
 * 而不必自己苦心孤诣地找id字段去创建ObjectIdentity,然后再到AclService里找acl时,若没有acl丫还给抛异常!
 * 
 * @author wenjian.liang
 */
public interface AclTypeCache {

	boolean hasAcl(String typeName);

	boolean hasAcl(Class<?> clazz);
}
