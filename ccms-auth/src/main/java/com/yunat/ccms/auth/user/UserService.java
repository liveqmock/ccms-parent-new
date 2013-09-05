package com.yunat.ccms.auth.user;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

public interface UserService {

	/**
	 * @param userName
	 * @param userType
	 * @return
	 */
	Set<GrantedAuthority> getAuthorities(User user);
}