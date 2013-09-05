package com.yunat.ccms.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCommandImpl implements UserCommand {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void saveOrUpdate(User user) {
		userRepository.saveAndFlush(user);
	}

}
