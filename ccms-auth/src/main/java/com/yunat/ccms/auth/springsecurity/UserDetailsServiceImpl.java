package com.yunat.ccms.auth.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;
import com.yunat.ccms.auth.user.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException, DataAccessException {
		final User user = getUser(userName);
		return new CCMSUserDetails(user);
	}

	private User getUser(final String userName) throws UsernameNotFoundException {
		try {
			final long id = Long.parseLong(userName);
			return userRepository.findOne(id);
		} catch (final NumberFormatException e) {
			throw new UsernameNotFoundException(userName + "不是id");
		}
	}
}
