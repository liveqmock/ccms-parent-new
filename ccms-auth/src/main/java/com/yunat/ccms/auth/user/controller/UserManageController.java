package com.yunat.ccms.auth.user.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Sets;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;
import com.yunat.ccms.auth.user.UserType;

/**
 * 系统管理-用户管理
 * 
 * @author yinwei
 * @author wenjian.liang
 * @version 1.0
 * @date 2013-04-02
 */

@Controller
@RequestMapping(value = "/user-manager/*")
public class UserManageController {

	// 内置用户类型
	private static final String userType = UserType.BUILD_IN.getName();

	@Autowired
	UserRepository userRepository;

	/**
	 * 获取系统内置的用户信息
	 * 
	 * @param userType
	 *            用户类型
	 * @return 用户列表
	 * @throws Exception
	 *             自定义往外层抛出异常信息交给异常解析器
	 */

	@RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<User> openUerList() throws Exception {
		try {
			return userRepository.queryByUserType(userType);
		} catch (final Exception e) {
			throw new Exception("获取用户列表失败！");
		}
	}

	/**
	 * @param userId
	 * @return
	 */
	public User findUserByOne(final Long userId) throws Exception {
		User user;
		try {
			user = userRepository.findOne(userId);
		} catch (final Exception e) {
			throw new Exception("获取用户失败！");
		}
		return user;
	}

	/**
	 * @param user
	 *            用户entity
	 * @param roleIds
	 *            该用户勾选的角色
	 * @throws Exception
	 *             自定义往外层抛出异常信息交给异常解析器
	 */

	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public void saveUser(final User user, final Long[] roleIds) throws Exception {
		try {
			//设置该用户的角色
			user.setRoles(constructCheckRoleSet(roleIds));
			userRepository.save(user);
		} catch (final Exception e) {
			throw new Exception("添加用户失败！");
		}

	}

	private Set<Role> constructCheckRoleSet(final Long[] roleIds) {
		final Set<Role> roleCheckSet = Sets.newHashSet();
		for (final Long roleId : roleIds) {
			final Role role = new Role();
			role.setId(roleId);
			roleCheckSet.add(role);
		}
		return roleCheckSet;
	}

	/**
	 * @param userId
	 *            用户ID
	 * @param switchFlag
	 *            是否禁用
	 * @throws Exception
	 *             自定义往外层抛出异常信息交给异常解析器
	 */
	@RequestMapping(value = "/user", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
	@ResponseBody
	public void editUser(@RequestParam final Long userId, @RequestParam final boolean switchFlag) throws Exception {
		try {
			final User user = findUserByOne(userId);
			user.setDisabled(switchFlag);
			userRepository.save(user);
		} catch (final Exception e) {
			throw new Exception("修改用户失败！");
		}
	}
}
