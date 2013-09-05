package com.yunat.ccms.auth.user;

import org.springframework.stereotype.Repository;

@Repository
public interface UserQuery {

	public User findByUsernameAndUsertype(String userName, String userType);

	public User findById(Long id);

}
