package com.yunat.ccms.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserQueryImpl implements UserQuery {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByUsernameAndUsertype(final String loginName, final String userType) {
		return userRepository.queryByLoginNameUserType(loginName, userType);
	}

	@Override
	public User findById(final Long id) {
		return userRepository.findOne(id);
	}

}
