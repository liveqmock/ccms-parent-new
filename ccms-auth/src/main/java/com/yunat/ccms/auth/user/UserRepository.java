package com.yunat.ccms.auth.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {

	@Query(" select u from User u where u.userType = :userType ")
	public List<User> queryByUserType(@Param("userType") String userType);

	@Query(" select u from User u where u.loginName=:loginName and u.userType = :userType ")
	public User queryByLoginNameUserType(@Param("loginName") String loginName, @Param("userType") String userType);

	@Query(" select u from User u where u.loginName=:loginName")
	public User queryByLoginName(@Param("loginName") String loginName);

	@Query(" select u from User u where u.loginName=:loginName and password=:password")
	public User queryByLoginNamePassword(@Param("loginName") String loginName, @Param("password") String password);

}
