package com.yunat.ccms.auth.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Service;

import com.yunat.ccms.auth.permission.Permission;
import com.yunat.ccms.auth.role.Role;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	/*
	 * (non-Javadoc)
	 * @see com.yunat.ccms.auth.user.UserService#getAuthorities(com.yunat.ccms.auth.user.User)
	 */
	@Override
	public Set<GrantedAuthority> getAuthorities(final User user) {
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
}
