package com.yunat.ccms.auth.springsecurity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.yunat.ccms.auth.permission.Permission;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.user.User;

public class CCMSUserDetails implements UserDetails {
	private static final long serialVersionUID = -2318405206147774203L;

	private final User user;

	public CCMSUserDetails(final User user) {
		super();
		this.user = user;
	}

	private static Set<GrantedAuthority> getAuthorities(final User user) {
		final Collection<Role> roles = user.getRoles();
		final Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		for (final Role role : roles) {
			final Set<Permission> permisssions = role.getPermisssions();
			for (final Permission p : permisssions) {
				grantedAuthorities.add(new GrantedAuthorityImpl(String.valueOf(p.getId())));
			}
		}
		return grantedAuthorities;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return getAuthorities(user);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return String.valueOf(user.getId());
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !user.getDisabled();
	}

	public User getUser() {
		return user;
	}
}