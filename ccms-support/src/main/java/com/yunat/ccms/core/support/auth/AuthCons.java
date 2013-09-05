package com.yunat.ccms.core.support.auth;

public interface AuthCons {

	String LOGIN_INFO_PATH = "/loginInfo";
	String LOGOUT_PATH = "/logout";

	String SUPPORT_OP_JSON_FIELD_NAME = "supportOps";
	String LOGIN_INFO_SESSION_ATTR_NAME = "l";

	int ACE_INDEX_OF_CURRENT_USER = 0;

	String ADD_ACL_FOR_SECURED_ANNOTATION = "addAcl";
	String DEL_ACL_FOR_SECURED_ANNOTATION = "delAcl";

	// 方法权限用的
	/**
	 * 
	 * 标注了<code>@Secured({AuthCons.SEC_ACL_WRITE_OR_ADMIN})</code>
	 * 的方法,会认为它是一个"修改某物"的方法,会对其进行acl验证.
	 */
	String SEC_ACL_WRITE_OR_ADMIN = "SEC_ACL_WRITE_OR_ADMIN";
	/**
	 * 
	 * 标注了<code>@Secured({AuthCons.SEC_ACL_CREATE_OR_ADMIN})</code>
	 * 的方法,会认为它是一个"创建某物"的方法,会对其进行acl验证.
	 */
	String SEC_ACL_CREATE_OR_ADMIN = "SEC_ACL_CREATE_OR_ADMIN";
	/**
	 * 
	 * 标注了<code>@Secured({AuthCons.SEC_ACL_DELETE_OR_ADMIN})</code>
	 * 的方法,会认为它是一个"删除某物"的方法,会对其进行acl验证.
	 */
	String SEC_ACL_DELETE_OR_ADMIN = "SEC_ACL_DELETE_OR_ADMIN";
}
