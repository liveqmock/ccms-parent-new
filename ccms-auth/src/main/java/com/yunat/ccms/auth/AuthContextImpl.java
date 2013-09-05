package com.yunat.ccms.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;

import com.yunat.ccms.auth.permission.Permission;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.cons.ProductEdition;

public class AuthContextImpl implements AuthContext {

	private final User user;
	private final HttpServletRequest request;
	private final ProductEdition productEdition;

	public AuthContextImpl(final User user, final HttpServletRequest request, final ProductEdition productEdition) {
		super();
		this.user = user;
		this.request = request;
		this.productEdition = productEdition;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public ProductEdition getProductEdition() {
		return productEdition;
	}

	@Override
	public Collection<Permission> getUserPermissions() {
		final Collection<Permission> rt = new ArrayList<Permission>();
		final Set<Role> roles = user.getRoles();
		if (roles != null) {
			for (final Role role : roles) {
				rt.addAll(role.getPermisssions());
			}
		}
		return rt;
	}

	@Override
	public Object getRequestParam(final String name) {
		return request.getParameter(name);
	}

	@Override
	public String getRequestHeader(final String name) {
		return request.getHeader(name);
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.valueOf(request.getMethod().toUpperCase());
	}

	//以下未支持
	@Override
	public Date getRequestTime() {
		return null;
	}

	@Override
	public String getRequestIp() {
		return null;
	}

	@Override
	public String getRequestReferer() {
		return null;
	}

}
